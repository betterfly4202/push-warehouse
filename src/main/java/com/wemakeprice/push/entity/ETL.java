package com.wemakeprice.push.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name= "tb_etl")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ETL {
    @Id
    private Long etlSeq;

    @Column(name ="table_name", nullable = false)
    private String tableName;

    @Column(name ="field_name", nullable = false)
    private String fieldName;

    @Column(name ="last_value", nullable = false)
    private Integer lastValue;
}
