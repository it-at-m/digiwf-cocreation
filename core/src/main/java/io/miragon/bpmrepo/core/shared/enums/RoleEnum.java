package io.miragon.bpmrepo.core.shared.enums;

public enum RoleEnum {

    /**
     * The owner is allowed to delete a repository
     * (and additionally has the privileges of an ADMIN, MEMBER, VIEWER)
     */
    OWNER(),

    /**
     * An admin is allowed to add/remove members and viewers
     * (and additionally has the privileges of an MEMBER, VIEWER)
     */
    ADMIN(),

    /**
     * A member is allowed to add/remove repository-objects
     * (and additionally has the privileges of a VIEWER)
     */
    MEMBER(),

    /**
     * A viewer is allowed to read repositories and repository-objects
     */
    VIEWER();

    /**
     * A user is allowed to add or remove an user,
     * if he/she is the owner or the admin of the repository
     * (an admin cannot add/remove a OWNER)
     */
    public boolean isUserAllowedToAddOrRemoveUser(final RoleEnum roleOfNewUser) {
        switch (this) {
        case OWNER:
            return true;

        case ADMIN:
            return roleOfNewUser != OWNER;

        default:
            return false;
        }
    }

    /**
     * A user is allowed to modify a repository if he/she
     * is the owner/admin of the repository
     */
    public boolean isUserAllowedToModifyRepository() {
        return this.equals(OWNER) || this.equals(ADMIN);
    }

    /**
     * A user is allowed to delete a repository if he/she is its owner
     */
    public boolean isUserAllowedToDeleteRepository() {
        return this.equals(OWNER);
    }

    /**
     * A user is allowed to create/update/delete a repository-object
     * if he/she has a role with more privileges than a 'VIEWER'
     */
    public boolean isUserAllowedToCrudRepositoryObjects() {
        return !this.equals(VIEWER);
    }

}


