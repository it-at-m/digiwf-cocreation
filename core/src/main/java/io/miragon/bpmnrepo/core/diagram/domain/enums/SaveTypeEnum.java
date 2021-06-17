package io.miragon.bpmnrepo.core.diagram.domain.enums;

public enum SaveTypeEnum {

    /**
     * Will increase the integer value
     */
    RELEASE(),


    /**
     * Will increase the decimal digit
     */

    MILESTONE(),

    /**
     * Won't affect the version number
     */

    AUTOSAVE();

}
