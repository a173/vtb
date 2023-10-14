/*
 * This file is generated by jOOQ.
 */
package org.vtb.jooq.tables;


import java.math.BigDecimal;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.vtb.jooq.Keys;
import org.vtb.jooq.Public;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Office extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.office</code>
     */
    public static final Office OFFICE = new Office();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.office.id</code>.
     */
    public final TableField<Record, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.office.sale_point_name</code>.
     */
    public final TableField<Record, String> SALE_POINT_NAME = createField(DSL.name("sale_point_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.office.sale_point_format</code>.
     */
    public final TableField<Record, String> SALE_POINT_FORMAT = createField(DSL.name("sale_point_format"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.office.address</code>.
     */
    public final TableField<Record, String> ADDRESS = createField(DSL.name("address"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.office.open_hours</code>.
     */
    public final TableField<Record, JSONB> OPEN_HOURS = createField(DSL.name("open_hours"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>public.office.rko</code>.
     */
    public final TableField<Record, String> RKO = createField(DSL.name("rko"), SQLDataType.VARCHAR(150), this, "");

    /**
     * The column <code>public.office.open_hours_individual</code>.
     */
    public final TableField<Record, JSONB> OPEN_HOURS_INDIVIDUAL = createField(DSL.name("open_hours_individual"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>public.office.office_type</code>.
     */
    public final TableField<Record, String> OFFICE_TYPE = createField(DSL.name("office_type"), SQLDataType.VARCHAR(150).nullable(false), this, "");

    /**
     * The column <code>public.office.latitude</code>.
     */
    public final TableField<Record, BigDecimal> LATITUDE = createField(DSL.name("latitude"), SQLDataType.NUMERIC(8, 6).nullable(false), this, "");

    /**
     * The column <code>public.office.longitude</code>.
     */
    public final TableField<Record, BigDecimal> LONGITUDE = createField(DSL.name("longitude"), SQLDataType.NUMERIC(8, 6).nullable(false), this, "");

    private Office(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Office(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.office</code> table reference
     */
    public Office(String alias) {
        this(DSL.name(alias), OFFICE);
    }

    /**
     * Create an aliased <code>public.office</code> table reference
     */
    public Office(Name alias) {
        this(alias, OFFICE);
    }

    /**
     * Create a <code>public.office</code> table reference
     */
    public Office() {
        this(DSL.name("office"), null);
    }

    public <O extends Record> Office(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, OFFICE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<Record, Long> getIdentity() {
        return (Identity<Record, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.OFFICE_PKEY;
    }

    @Override
    public Office as(String alias) {
        return new Office(DSL.name(alias), this);
    }

    @Override
    public Office as(Name alias) {
        return new Office(alias, this);
    }

    @Override
    public Office as(Table<?> alias) {
        return new Office(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Office rename(String name) {
        return new Office(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Office rename(Name name) {
        return new Office(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Office rename(Table<?> name) {
        return new Office(name.getQualifiedName(), null);
    }
}
