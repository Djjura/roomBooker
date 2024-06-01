/*
 * This file is generated by jOOQ.
 */
package database.tables;


import database.Diplomski;
import database.Keys;
import database.tables.records.RoomRecord;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Room extends TableImpl<RoomRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>diplomski.room</code>
     */
    public static final Room ROOM = new Room();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoomRecord> getRecordType() {
        return RoomRecord.class;
    }

    /**
     * The column <code>diplomski.room.uuid</code>.
     */
    public final TableField<RoomRecord, String> UUID = createField(DSL.name("uuid"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>diplomski.room.NAME</code>.
     */
    public final TableField<RoomRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(45).nullable(false), this, "");

    /**
     * The column <code>diplomski.room.Type</code>.
     */
    public final TableField<RoomRecord, String> TYPE = createField(DSL.name("Type"), SQLDataType.VARCHAR(45).nullable(false), this, "");

    /**
     * The column <code>diplomski.room.Maximum_occupancy</code>.
     */
    public final TableField<RoomRecord, String> MAXIMUM_OCCUPANCY = createField(DSL.name("Maximum_occupancy"), SQLDataType.VARCHAR(45), this, "");

    /**
     * The column <code>diplomski.room.Equipment</code>.
     */
    public final TableField<RoomRecord, String> EQUIPMENT = createField(DSL.name("Equipment"), SQLDataType.VARCHAR(300), this, "");

    private Room(Name alias, Table<RoomRecord> aliased) {
        this(alias, aliased, null);
    }

    private Room(Name alias, Table<RoomRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>diplomski.room</code> table reference
     */
    public Room(String alias) {
        this(DSL.name(alias), ROOM);
    }

    /**
     * Create an aliased <code>diplomski.room</code> table reference
     */
    public Room(Name alias) {
        this(alias, ROOM);
    }

    /**
     * Create a <code>diplomski.room</code> table reference
     */
    public Room() {
        this(DSL.name("room"), null);
    }

    public <O extends Record> Room(Table<O> child, ForeignKey<O, RoomRecord> key) {
        super(child, key, ROOM);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Diplomski.DIPLOMSKI;
    }

    @Override
    public UniqueKey<RoomRecord> getPrimaryKey() {
        return Keys.KEY_ROOM_PRIMARY;
    }

    @Override
    public List<UniqueKey<RoomRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_ROOM_NAME_UNIQUE);
    }

    @Override
    public Room as(String alias) {
        return new Room(DSL.name(alias), this);
    }

    @Override
    public Room as(Name alias) {
        return new Room(alias, this);
    }

    @Override
    public Room as(Table<?> alias) {
        return new Room(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Room rename(String name) {
        return new Room(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Room rename(Name name) {
        return new Room(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Room rename(Table<?> name) {
        return new Room(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
