package io.miragon.bpmrepo.core.diagram.domain.enums;

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
