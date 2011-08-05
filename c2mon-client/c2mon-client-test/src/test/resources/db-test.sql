CREATE TABLE dmn_equipment_v (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    equipment_name VARCHAR(4000),
    equipment_rule_tag_id INTEGER NOT NULL,
    CONSTRAINT TUC_MAP_DEF_1 UNIQUE (equipment_name,equipment_rule_tag_id )
);


insert into dmn_equipment_v (equipment_name,equipment_rule_tag_id) values ('TESTDEVICE1',100);
insert into dmn_equipment_v (equipment_name,equipment_rule_tag_id) values ('TESTDEVICE2',222);

