
    drop table Attachment cascade constraints;

    drop table BooleanExpression cascade constraints;

    drop table Content cascade constraints;

    drop table Deadline cascade constraints;

    drop table Delegation_delegates cascade constraints;

    drop table Escalation cascade constraints;

    drop table EventTypes cascade constraints;

    drop table I18NText cascade constraints;

    drop table NodeInstanceLog cascade constraints;

    drop table Notification cascade constraints;

    drop table Notification_BAs cascade constraints;

    drop table Notification_Recipients cascade constraints;

    drop table Notification_email_header cascade constraints;

    drop table OrganizationalEntity cascade constraints;

    drop table PeopleAssignments_BAs cascade constraints;

    drop table PeopleAssignments_ExclOwners cascade constraints;

    drop table PeopleAssignments_PotOwners cascade constraints;

    drop table PeopleAssignments_Recipients cascade constraints;

    drop table PeopleAssignments_Stakeholders cascade constraints;

    drop table ProcessInstanceInfo cascade constraints;

    drop table ProcessInstanceLog cascade constraints;

    drop table Reassignment cascade constraints;

    drop table Reassignment_potentialOwners cascade constraints;

    drop table SessionInfo cascade constraints;

    drop table SubTasksStrategy cascade constraints;

    drop table Task cascade constraints;

    drop table VariableInstanceLog cascade constraints;

    drop table WorkItemInfo cascade constraints;

    drop table email_header cascade constraints;

    drop table task_comment cascade constraints;

    drop sequence ATTACHMENT_ID_SEQ;

    drop sequence BOOLEANEXPR_ID_SEQ;

    drop sequence COMMENT_ID_SEQ;

    drop sequence CONTENT_ID_SEQ;

    drop sequence DEADLINE_ID_SEQ;

    drop sequence EMAILNOTIFHEAD_ID_SEQ;

    drop sequence ESCALATION_ID_SEQ;

    drop sequence I18NTEXT_ID_SEQ;

    drop sequence NODE_INST_LOG_ID_SEQ;

    drop sequence NOTIFICATION_ID_SEQ;

    drop sequence PROC_INST_LOG_ID_SEQ;

    drop sequence REASSIGNMENT_ID_SEQ;

    drop sequence SESSIONINFO_ID_SEQ;

    drop sequence VAR_INST_LOG_ID_SEQ;

    drop sequence WORKITEMINFO_ID_SEQ;

    drop sequence hibernate_sequence;

    create table Attachment (
        id number(19,0) not null,
        accessType number(10,0),
        attachedAt timestamp,
        attachmentContentId number(19,0) not null,
        contentType varchar2(255 char),
        name varchar2(255 char),
        attachment_size number(10,0),
        attachedBy_id varchar2(255 char),
        TaskData_Attachments_Id number(19,0),
        primary key (id)
    );

    create table BooleanExpression (
        id number(19,0) not null,
        expression clob,
        type varchar2(255 char),
        Escalation_Constraints_Id number(19,0),
        primary key (id)
    );

    create table Content (
        id number(19,0) not null,
        content blob,
        primary key (id)
    );

    create table Deadline (
        id number(19,0) not null,
        deadline_date timestamp,
        escalated number(5,0),
        Deadlines_StartDeadLine_Id number(19,0),
        Deadlines_EndDeadLine_Id number(19,0),
        primary key (id)
    );

    create table Delegation_delegates (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table Escalation (
        id number(19,0) not null,
        name varchar2(255 char),
        Deadline_Escalation_Id number(19,0),
        primary key (id)
    );

    create table EventTypes (
        InstanceId number(19,0) not null,
        element varchar2(255 char)
    );

    create table I18NText (
        id number(19,0) not null,
        language varchar2(255 char),
        shortText varchar2(255 char),
        text clob,
        Task_Subjects_Id number(19,0),
        Task_Names_Id number(19,0),
        Task_Descriptions_Id number(19,0),
        Reassignment_Documentation_Id number(19,0),
        Notification_Subjects_Id number(19,0),
        Notification_Names_Id number(19,0),
        Notification_Documentation_Id number(19,0),
        Notification_Descriptions_Id number(19,0),
        Deadline_Documentation_Id number(19,0),
        primary key (id)
    );

    create table NodeInstanceLog (
        id number(19,0) not null,
        log_date timestamp,
        nodeId varchar2(255 char),
        nodeInstanceId varchar2(255 char),
        nodeName varchar2(255 char),
        processId varchar2(255 char),
        processInstanceId number(19,0) not null,
        type number(10,0) not null,
        primary key (id)
    );

    create table Notification (
        DTYPE varchar2(31 char) not null,
        id number(19,0) not null,
        priority number(10,0) not null,
        Escalation_Notifications_Id number(19,0),
        primary key (id)
    );

    create table Notification_BAs (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table Notification_Recipients (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table Notification_email_header (
        Notification_id number(19,0) not null,
        emailHeaders_id number(19,0) not null,
        mapkey varchar2(255 char) not null,
        primary key (Notification_id, mapkey),
        unique (emailHeaders_id)
    );

    create table OrganizationalEntity (
        DTYPE varchar2(31 char) not null,
        id varchar2(255 char) not null,
        primary key (id)
    );

    create table PeopleAssignments_BAs (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table PeopleAssignments_ExclOwners (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table PeopleAssignments_PotOwners (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table PeopleAssignments_Recipients (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table PeopleAssignments_Stakeholders (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table ProcessInstanceInfo (
        InstanceId number(19,0) not null,
        OPTLOCK number(10,0),
        processId varchar2(255 char),
        startDate timestamp,
        lastReadDate timestamp,
        lastModificationDate timestamp,
        state number(10,0) not null,
        processInstanceByteArray blob,
        primary key (InstanceId)
    );

    create table ProcessInstanceLog (
        id number(19,0) not null,
        end_date timestamp,
        outcome varchar2(255 char),
        parentProcessInstanceId number(19,0),
        processId varchar2(255 char),
        processInstanceId number(19,0) not null,
        start_date timestamp,
        status number(10,0),
        primary key (id)
    );

    create table Reassignment (
        id number(19,0) not null,
        Escalation_Reassignments_Id number(19,0),
        primary key (id)
    );

    create table Reassignment_potentialOwners (
        task_id number(19,0) not null,
        entity_id varchar2(255 char) not null
    );

    create table SessionInfo (
        id number(10,0) not null,
        lastModificationDate timestamp,
        rulesByteArray blob,
        startDate timestamp,
        OPTLOCK number(10,0),
        primary key (id)
    );

    create table SubTasksStrategy (
        DTYPE varchar2(100 char) not null,
        id number(19,0) not null,
        name varchar2(255 char),
        Task_Id number(19,0),
        primary key (id)
    );

    create table Task (
        id number(19,0) not null,
        archived number(5,0),
        allowedToDelegate varchar2(255 char),
        priority number(10,0) not null,
        activationTime timestamp,
        completedOn timestamp,
        createdOn timestamp,
        documentAccessType number(10,0),
        documentContentId number(19,0) not null,
        documentType varchar2(255 char),
        expirationTime timestamp,
        faultAccessType number(10,0),
        faultContentId number(19,0) not null,
        faultName varchar2(255 char),
        faultType varchar2(255 char),
        outputAccessType number(10,0),
        outputContentId number(19,0) not null,
        outputType varchar2(255 char),
        parentId number(19,0) not null,
        previousStatus number(10,0),
        processId varchar2(255 char),
        processInstanceId number(19,0) not null,
        processSessionId number(10,0) not null,
        skipable number(1,0) not null,
        status varchar2(255 char),
        workItemId number(19,0) not null,
        OPTLOCK number(10,0),
        taskInitiator_id varchar2(255 char),
        actualOwner_id varchar2(255 char),
        createdBy_id varchar2(255 char),
        primary key (id)
    );

    create table VariableInstanceLog (
        id number(19,0) not null,
        log_date timestamp,
        processId varchar2(255 char),
        processInstanceId number(19,0) not null,
        value varchar2(255 char),
        variableId varchar2(255 char),
        variableInstanceId varchar2(255 char),
        primary key (id)
    );

    create table WorkItemInfo (
        workItemId number(19,0) not null,
        creationDate timestamp,
        name varchar2(255 char),
        processInstanceId number(19,0) not null,
        state number(19,0) not null,
        OPTLOCK number(10,0),
        workItemByteArray blob,
        primary key (workItemId)
    );

    create table email_header (
        id number(19,0) not null,
        body clob,
        fromAddress varchar2(255 char),
        language varchar2(255 char),
        replyToAddress varchar2(255 char),
        subject varchar2(255 char),
        primary key (id)
    );

    create table task_comment (
        id number(19,0) not null,
        addedAt timestamp,
        text clob,
        addedBy_id varchar2(255 char),
        TaskData_Comments_Id number(19,0),
        primary key (id)
    );

    alter table Attachment 
        add constraint FK1C935438EF5F064 
        foreign key (attachedBy_id) 
        references OrganizationalEntity;

    alter table Attachment 
        add constraint FK1C93543F21826D9 
        foreign key (TaskData_Attachments_Id) 
        references Task;

    alter table BooleanExpression 
        add constraint FKE3D208C0AFB75F7D 
        foreign key (Escalation_Constraints_Id) 
        references Escalation;

    alter table Deadline 
        add constraint FK21DF3E78684BACA3 
        foreign key (Deadlines_StartDeadLine_Id) 
        references Task;

    alter table Deadline 
        add constraint FK21DF3E7827ABEB8A 
        foreign key (Deadlines_EndDeadLine_Id) 
        references Task;

    alter table Delegation_delegates 
        add constraint FK47485D572C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table Delegation_delegates 
        add constraint FK47485D5736B2F154 
        foreign key (task_id) 
        references Task;

    alter table Escalation 
        add constraint FK67B2C6B5C7A04C70 
        foreign key (Deadline_Escalation_Id) 
        references Deadline;

    alter table EventTypes 
        add constraint FKB0E5621F7665489A 
        foreign key (InstanceId) 
        references ProcessInstanceInfo;

    alter table I18NText 
        add constraint FK2349686B2162DFB4 
        foreign key (Notification_Descriptions_Id) 
        references Notification;

    alter table I18NText 
        add constraint FK2349686BD488CEEB 
        foreign key (Notification_Names_Id) 
        references Notification;

    alter table I18NText 
        add constraint FK2349686B5EEBB6D9 
        foreign key (Reassignment_Documentation_Id) 
        references Reassignment;

    alter table I18NText 
        add constraint FK2349686B3330F6D9 
        foreign key (Deadline_Documentation_Id) 
        references Deadline;

    alter table I18NText 
        add constraint FK2349686B8046A239 
        foreign key (Notification_Documentation_Id) 
        references Notification;

    alter table I18NText 
        add constraint FK2349686B69B21EE8 
        foreign key (Task_Descriptions_Id) 
        references Task;

    alter table I18NText 
        add constraint FK2349686BB2FA6B18 
        foreign key (Task_Subjects_Id) 
        references Task;

    alter table I18NText 
        add constraint FK2349686B98B62B 
        foreign key (Task_Names_Id) 
        references Task;

    alter table I18NText 
        add constraint FK2349686BF952CEE4 
        foreign key (Notification_Subjects_Id) 
        references Notification;

    alter table Notification 
        add constraint FK2D45DD0B3E0890B 
        foreign key (Escalation_Notifications_Id) 
        references Escalation;

    alter table Notification_BAs 
        add constraint FK2DD68EE02C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table Notification_BAs 
        add constraint FK2DD68EE09C76EABA 
        foreign key (task_id) 
        references Notification;

    alter table Notification_Recipients 
        add constraint FK98FD214E2C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table Notification_Recipients 
        add constraint FK98FD214E9C76EABA 
        foreign key (task_id) 
        references Notification;

    alter table Notification_email_header 
        add constraint FKF30FE3441F7B912A 
        foreign key (emailHeaders_id) 
        references email_header;

    alter table Notification_email_header 
        add constraint FKF30FE34430BE501C 
        foreign key (Notification_id) 
        references Notification;

    alter table PeopleAssignments_BAs 
        add constraint FK9D8CF4EC2C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table PeopleAssignments_BAs 
        add constraint FK9D8CF4EC36B2F154 
        foreign key (task_id) 
        references Task;

    alter table PeopleAssignments_ExclOwners 
        add constraint FKC77B97E42C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table PeopleAssignments_ExclOwners 
        add constraint FKC77B97E436B2F154 
        foreign key (task_id) 
        references Task;

    alter table PeopleAssignments_PotOwners 
        add constraint FK1EE418D2C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table PeopleAssignments_PotOwners 
        add constraint FK1EE418D36B2F154 
        foreign key (task_id) 
        references Task;

    alter table PeopleAssignments_Recipients 
        add constraint FKC6F615C22C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table PeopleAssignments_Recipients 
        add constraint FKC6F615C236B2F154 
        foreign key (task_id) 
        references Task;

    alter table PeopleAssignments_Stakeholders 
        add constraint FK482F79D52C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table PeopleAssignments_Stakeholders 
        add constraint FK482F79D536B2F154 
        foreign key (task_id) 
        references Task;

    alter table Reassignment 
        add constraint FK724D0560A5C17EE0 
        foreign key (Escalation_Reassignments_Id) 
        references Escalation;

    alter table Reassignment_potentialOwners 
        add constraint FK90B59CFF2C122ED2 
        foreign key (entity_id) 
        references OrganizationalEntity;

    alter table Reassignment_potentialOwners 
        add constraint FK90B59CFFE17E130F 
        foreign key (task_id) 
        references Reassignment;

    alter table SubTasksStrategy 
        add constraint FKDE5DF2E136B2F154 
        foreign key (Task_Id) 
        references Task;

    alter table Task 
        add constraint FK27A9A59E619A0 
        foreign key (createdBy_id) 
        references OrganizationalEntity;

    alter table Task 
        add constraint FK27A9A56CE1EF3A 
        foreign key (actualOwner_id) 
        references OrganizationalEntity;

    alter table Task 
        add constraint FK27A9A5F213F8B5 
        foreign key (taskInitiator_id) 
        references OrganizationalEntity;

    alter table task_comment 
        add constraint FK61F475A5B35E68F5 
        foreign key (TaskData_Comments_Id) 
        references Task;

    alter table task_comment 
        add constraint FK61F475A52FF04688 
        foreign key (addedBy_id) 
        references OrganizationalEntity;

    create sequence ATTACHMENT_ID_SEQ;

    create sequence BOOLEANEXPR_ID_SEQ;

    create sequence COMMENT_ID_SEQ;

    create sequence CONTENT_ID_SEQ;

    create sequence DEADLINE_ID_SEQ;

    create sequence EMAILNOTIFHEAD_ID_SEQ;

    create sequence ESCALATION_ID_SEQ;

    create sequence I18NTEXT_ID_SEQ;

    create sequence NODE_INST_LOG_ID_SEQ;

    create sequence NOTIFICATION_ID_SEQ;

    create sequence PROC_INST_LOG_ID_SEQ;

    create sequence REASSIGNMENT_ID_SEQ;

    create sequence SESSIONINFO_ID_SEQ;

    create sequence VAR_INST_LOG_ID_SEQ;

    create sequence WORKITEMINFO_ID_SEQ;

    create sequence hibernate_sequence;
