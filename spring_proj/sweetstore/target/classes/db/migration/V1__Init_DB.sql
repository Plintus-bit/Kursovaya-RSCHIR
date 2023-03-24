create table goods (
    id integer not null auto_increment,
    article integer not null,
    cost integer,
    count integer,
    descript varchar(255),
    name varchar(255),
    url varchar(255),
    subtype_id integer not null,
    primary key (id)
);

create table good_subtypes (
    id integer not null auto_increment,
    name varchar(255),
    parent_id integer,
    primary key (id)
);

create table good_types (
    id integer not null auto_increment,
    name varchar(255),
    url varchar(255),
    primary key (id)
);

create table ingredients (
    id integer not null auto_increment,
    name varchar(255),
    primary key (id)
);

create table ing_structures (
    id integer not null auto_increment,
    good_id integer not null,
    ing_id integer not null,
    primary key (id)
);

create table method_types (
    id integer not null auto_increment,
    name varchar(255),
    primary key (id)
);

create table nutritions (
    id integer not null auto_increment,
    caloric float(23),
    carbhyd float(23),
    diet_fiber float(23),
    fats float(23),
    proteins float(23),
    water float(23),
    good_id integer not null,
    primary key (id)
);

create table order_good_structs (
    id integer not null auto_increment,
    count integer,
    good_id integer not null,
    order_id integer not null,
    primary key (id)
);

create table order_statuses (
    id integer not null auto_increment,
    name varchar(255),
    primary key (id)
);

create table product_methods (
    id integer not null auto_increment,
    address varchar(255),
    method_id integer not null,
    primary key (id)
);

create table user_role (
    user_id bigint not null,
    roles varchar(255)
);

create table user_orders (
    id integer not null auto_increment,
    order_date datetime(6),
    customer_id bigint,
    prm_id integer,
    status_id integer,
    primary key (id)
);

create table usr (
    id bigint not null auto_increment,
    activation_code varchar(255),
    cust_fullname varchar(200),
    active bit not null,
    phone varchar(20),
    email varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (id)
);

alter table ingredients add constraint ing_uk unique (name);
alter table goods add constraint good_uk unique (article);
alter table method_types add constraint method_type_uk unique (name);
alter table order_statuses add constraint order_statuses_uk unique (name);
alter table goods add constraint good_subtype_fk foreign key (subtype_id) references good_subtypes (id);
alter table good_subtypes add constraint subtype_type_fk foreign key (parent_id) references good_types (id);
alter table ing_structures add constraint ingstruct_goods_fk foreign key (good_id) references goods (id);
alter table ing_structures add constraint ingstruct_ings_fk foreign key (ing_id) references ingredients (id);
alter table nutritions add constraint nutr_good_fk foreign key (good_id) references goods (id);
alter table order_good_structs add constraint ogs_goods_fk foreign key (good_id) references goods (id);
alter table order_good_structs add constraint ogs_orders_fk foreign key (order_id) references user_orders (id);
alter table product_methods add constraint prmethods_methodtypes_fk foreign key (method_id) references method_types (id);
alter table user_role add constraint role_usr_fk foreign key (user_id) references usr (id);
alter table user_orders add constraint orders_usr_fk foreign key (customer_id) references usr (id);
alter table user_orders add constraint orders_prmethods_fk foreign key (prm_id) references product_methods (id);
alter table user_orders add constraint orders_statuses_fk foreign key (status_id) references order_statuses (id);