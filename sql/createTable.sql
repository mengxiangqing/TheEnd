create table choose_class
(
    id          bigint auto_increment
        primary key,
    student_id  bigint                             not null,
    course_id   bigint                             not null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    create_user bigint   default 0                 not null,
    update_user bigint   default 0                 not null,
    is_delete   tinyint  default 0                 not null comment '逻辑删除，默认0',
    remark      varchar(255)                       null
);

create table classroom
(
    id          bigint auto_increment
        primary key,
    room_name   varchar(100)                         null comment '教室详细名字',
    capacity    int                                  null comment '教室容量',
    humans      int                                  null comment '现有人数',
    address     varchar(50)                          null comment '所在教学楼',
    room_status tinyint(1) default 0                 null comment '教室状态，0-正常，1-有课，2-教室关闭',
    create_time datetime   default CURRENT_TIMESTAMP not null,
    create_user bigint     default 0                 not null,
    update_time datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_user bigint     default 0                 not null,
    remark      varchar(255)                         null,
    is_delete   tinyint    default 0                 not null,
    seat_rate   double     default 0                 null comment '入座率'
);

create table course
(
    id                  bigint auto_increment comment '课程ID'
        primary key,
    course_number       varchar(20)                        null comment '课程号',
    course_name         varchar(50)                        null comment '课程名字',
    teaching_time       varchar(20)                        null comment '授课学期',
    college             varchar(100)                       null comment '所属单位',
    phone               varchar(11)                        null comment '联系电话',
    teacher             bigint                             null comment '授课教师ID',
    classroom_id        bigint                             null comment '授课教室',
    choose_num          int      default 0                 null comment '选课人数',
    start_week          tinyint                            null comment '起始周',
    end_week            tinyint                            null comment '结束周',
    average_up_rate     float                              null comment '平均抬头率',
    average_attend_rate float                              null comment '平均到课率',
    average_front_rate  float                              null comment '平均前排率',
    create_time         datetime default CURRENT_TIMESTAMP not null,
    create_user         bigint   default 0                 not null,
    update_time         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_user         bigint   default 0                 not null,
    remark              varchar(255)                       null,
    is_delete           tinyint  default 0                 not null
);

create table single_class
(
    id           bigint auto_increment comment '单节课id',
    course_id    bigint                             null comment '课程ID',
    teacher_id   bigint                             null comment '授课教师ID',
    start_time   datetime                           null comment '上课时间',
    end_time     datetime                           null comment '下课时间',
    up_rates     varchar(255)                       null comment '抬头率列表',
    attend_rates varchar(255)                       null comment '到课率列表',
    front_rates  varchar(255)                       null comment '前排率列表',
    create_time  datetime default CURRENT_TIMESTAMP not null,
    create_user  bigint   default 0                 not null,
    update_time  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    update_user  bigint   default 0                 not null,
    remark       varchar(255)                       null,
    is_delete    tinyint  default 0                 not null,
    up_rate      double                             null,
    attend_rate  double                             null,
    front_rate   double                             null,
    primary key (id, is_delete)
);

create table teaching_relationship
(
    id          bigint auto_increment
        primary key,
    teacher_id  bigint                             not null,
    course_id   bigint                             not null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    is_delete   tinyint  default 0                 not null comment '是否删除，默认值为0',
    create_user bigint   default 0                 not null,
    update_user bigint   default 0                 not null,
    remark      varchar(255)                       null
);

create table user
(
    id            bigint auto_increment comment '用户id'
        primary key,
    user_account  varchar(20)                          not null comment '学工号',
    user_name     varchar(50)                          null comment '用户姓名',
    title         varchar(50)                          null comment '教职工职称',
    grade         int                                  null comment '入校年份',
    user_password varchar(50)                          not null comment '登录密码',
    user_role     tinyint(1) default 1                 null comment '用户角色：1-学生，2-教师，3-督导员，4-管理员',
    user_status   tinyint(1) default 0                 null comment '用户状态：0-正常，1-学生毕业，2-学生结业，3-学生肄业，4-教职工离职，5-教职工退休',
    gender        tinyint(1)                           null comment '性别，0-女，1-男',
    birth         varchar(8)                           null comment '出生年月',
    college       varchar(100)                         null comment '所属单位',
    stu_class     varchar(100)                         null comment '所属班级',
    phone         varchar(11)                          null comment '联系电话',
    email         varchar(50)                          null comment '电子邮箱',
    create_time   datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    create_user   bigint     default 0                 not null comment '创建人，默认为0表示用户自己创建',
    update_time   datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user   bigint     default 0                 not null comment '更新人',
    is_delete     tinyint(1) default 0                 not null comment '是否删除',
    remark        varchar(255)                         null comment '备注'
);

