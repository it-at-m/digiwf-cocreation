package io.miragon.bpmnrepo.core.repository.domain.business;

import io.miragon.bpmnrepo.core.repository.api.transport.AssignmentWithUserNameTO;
import io.miragon.bpmnrepo.core.repository.domain.mapper.AssignmentMapper;
import io.miragon.bpmnrepo.core.repository.domain.model.Assignment;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.AssignmentId;
import io.miragon.bpmnrepo.core.repository.infrastructure.entity.AssignmentEntity;
import io.miragon.bpmnrepo.core.shared.enums.RoleEnum;
import io.miragon.bpmnrepo.core.shared.exception.AccessRightException;
import io.miragon.bpmnrepo.core.repository.infrastructure.repository.AssignmentJpa;
import io.miragon.bpmnrepo.core.repository.api.transport.AssignmentTO;
import io.miragon.bpmnrepo.core.user.domain.business.UserService;
import io.miragon.bpmnrepo.core.user.domain.mapper.UserMapper;
import io.miragon.bpmnrepo.core.user.domain.model.User;
import io.miragon.bpmnrepo.core.user.infrastructure.entity.UserEntity;
import io.miragon.bpmnrepo.core.user.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentJpa assignmentJpa;
    private final UserJpaRepository userJpa;
    private final AuthService authService;
    private final UserService userService;
    private final AssignmentMapper mapper;
    private final UserMapper userMapper;
    private final BpmnRepositoryService bpmnRepositoryService;


    public void createOrUpdateAssignment(AssignmentWithUserNameTO assignmentWithUserNameTO){
        this.authService.checkIfOperationIsAllowed(assignmentWithUserNameTO.getBpmnRepositoryId(), RoleEnum.ADMIN);
        String newAssignmentUserId = this.userService.getUserIdByUsername(assignmentWithUserNameTO.getUserName());
        String bpmnRepositoryId = assignmentWithUserNameTO.getBpmnRepositoryId();
        String username = assignmentWithUserNameTO.getUserName();

        RoleEnum newRole = assignmentWithUserNameTO.getRoleEnum();
        AssignmentTO assignmentTO = new AssignmentTO(bpmnRepositoryId, newAssignmentUserId, username, newRole);
        AssignmentEntity assignmentEntity = assignmentJpa.findByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(assignmentTO.getBpmnRepositoryId(), newAssignmentUserId);
        if(assignmentEntity == null){
            createAssignment(assignmentTO);
            log.debug("Created assignment");
        }
        else{
            updateAssignment(assignmentTO);
            log.debug(String.format("Updated role to %s", assignmentTO.getRoleEnum().toString()));
        }
    }


    public void createAssignment(AssignmentTO assignmentTO){
        Assignment assignment = this.mapper.toModel(assignmentTO);
        AssignmentId assignmentId = this.mapper.toEmbeddable(assignment.getUserId(), assignment.getBpmnRepositoryId());
        AssignmentEntity assignmentEntity = this.mapper.toEntity(assignment, assignmentId);
        this.saveToDb(assignmentEntity);
        Integer assignedUsers = this.assignmentJpa.countByAssignmentId_BpmnRepositoryId(assignment.getBpmnRepositoryId());
        this.bpmnRepositoryService.updateAssignedUsers(assignment.getBpmnRepositoryId(), assignedUsers);
    }

    //0: Owner, 1: Admin, 2: Member, 3: Viewer
    public void updateAssignment(AssignmentTO assignmentTO) {
        //Assignments can be managed by Admins and Owners
        String newAssignmentUserId = assignmentTO.getUserId();
        String currentUserId = this.userService.getUserIdOfCurrentUser();
        this.authService.checkIfOperationIsAllowed(assignmentTO.getBpmnRepositoryId(), RoleEnum.ADMIN);

        RoleEnum currentUserRole = this.getUserRole(assignmentTO.getBpmnRepositoryId(), currentUserId);
        RoleEnum affectedUserRole = this.getUserRole(assignmentTO.getBpmnRepositoryId(), newAssignmentUserId);

        //Exception if the user tries to change its own rights
        if (newAssignmentUserId == currentUserId) {
            throw new AccessRightException("You can't change your own role");
        }
        //Exception if the user tries to change the role of someone with higher permissions (if an admin tries to change the role of an owner)
        if(affectedUserRole.ordinal() < currentUserRole.ordinal()){
            throw new AccessRightException(String.format("You cant change the role of %s because your role provides less rights (You are an \"%s\")", this.getUserRole(assignmentTO.getBpmnRepositoryId(), newAssignmentUserId), this.getUserRole(assignmentTO.getBpmnRepositoryId(), this.userService.getUserIdOfCurrentUser())));
        }
        //Exception if the user tries to give someone a role that is higher than its own
        if(currentUserRole.ordinal() > assignmentTO.getRoleEnum().ordinal()){
            throw new AccessRightException("You can't assign roles with higher permissions than your own");
        }
        Assignment assignment = new Assignment(assignmentTO);
        AssignmentId assignmentId = this.mapper.toEmbeddable(assignment.getUserId(), assignment.getBpmnRepositoryId());
        AssignmentEntity assignmentEntity = this.mapper.toEntity(assignment, assignmentId);
        this.saveToDb(assignmentEntity);
    }


    public void createInitialAssignment(String bpmnRepositoryId){
        String currentUserId = this.userService.getUserIdOfCurrentUser();
        UserEntity currentUserEntity = this.userJpa.findByUserId(currentUserId);
        User currentUser = this.userMapper.toModel(currentUserEntity);
        String currentUserName = currentUser.getUserName();
        AssignmentTO assignmentTO = new AssignmentTO(bpmnRepositoryId, currentUserId, currentUserName, RoleEnum.OWNER);
        Assignment assignment = new Assignment(assignmentTO);
        AssignmentEntity assignmentEntity = this.mapper.toEntity(assignment, this.mapper.toEmbeddable(currentUserId, bpmnRepositoryId));
        this.assignmentJpa.save(assignmentEntity);
    }

    //receive all AssignmentEntities related to the user
    public List<String> getAllAssignedRepositoryIds(String userId){
        return this.assignmentJpa.findAssignmentEntitiesByAssignmentId_UserIdEquals(userId).stream()
                .map(assignmentEntity -> assignmentEntity.getAssignmentId().getBpmnRepositoryId())
                .collect(Collectors.toList());
    }


    public AssignmentEntity getAssignmentEntity(String bpmnRepositoryId, String userId){
        return this.assignmentJpa.findByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(bpmnRepositoryId, userId);
    }


    public RoleEnum getUserRole(String bpmnRepositoryId, String userId){
       AssignmentEntity assignmentEntity = this.getAssignmentEntity(bpmnRepositoryId, userId);
        return assignmentEntity.getRoleEnum();
    }

    public List<AssignmentTO> getAllAssignedUsers(String bpmnRepositoryId){
        authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.MEMBER);
        List<AssignmentEntity> assignments = assignmentJpa.findByAssignmentId_BpmnRepositoryId(bpmnRepositoryId);
        List<AssignmentTO> assignedUsers = assignments.stream()
                .map(assignmentEntity -> mapper.toTO(mapper.toModel(assignmentEntity)))
                .collect(Collectors.toList());
        return assignedUsers;
    }


    public void saveToDb(AssignmentEntity assignmentEntity){
        authService.checkIfOperationIsAllowed(assignmentEntity.getAssignmentId().getBpmnRepositoryId(), RoleEnum.ADMIN);
        assignmentJpa.save(assignmentEntity);
    }


    public void deleteAssignment(String bpmnRepositoryId, String deletedUsername){
        String deletedUserId = this.userService.getUserIdByUsername(deletedUsername);
        String currentUserId = this.userService.getUserIdOfCurrentUser();
        this.authService.checkIfOperationIsAllowed(bpmnRepositoryId, RoleEnum.ADMIN);

        RoleEnum currentUserRole = this.getUserRole(bpmnRepositoryId, currentUserId);
        RoleEnum deletedUserRole = this.getUserRole(bpmnRepositoryId, deletedUserId);
        //role of deleted user has to be equal or weaker than role of current user (0:Owner, 1:Admin, 2:Member, 3: Viewer)
        if(currentUserRole.ordinal() > deletedUserRole.ordinal()){
            throw new AccessRightException(String.format("You cant remove %s (Repository-%s) from this repository because your role provides less rights (You are an %s)",
                    deletedUsername,
                    deletedUserRole,
                    currentUserRole));
        }
        this.assignmentJpa.deleteAssignmentEntityByAssignmentId_BpmnRepositoryIdAndAssignmentId_UserId(bpmnRepositoryId, deletedUserId);
        Integer assignedUsers = this.assignmentJpa.countByAssignmentId_BpmnRepositoryId(bpmnRepositoryId);
        this.bpmnRepositoryService.updateAssignedUsers(bpmnRepositoryId, assignedUsers);
    }


    public void deleteAllByRepositoryId(String bpmnRepositoryId){
        //Auth check in RepositoryFacade
        //Is only called if the corresponding repository is deleted
        int deletedAssignments = this.assignmentJpa.deleteAllByAssignmentId_BpmnRepositoryId(bpmnRepositoryId);
        log.debug(String.format("Deleted Assignments for all %s users", deletedAssignments));
    }
}
