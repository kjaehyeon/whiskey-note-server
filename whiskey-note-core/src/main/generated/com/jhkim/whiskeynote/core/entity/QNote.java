package com.jhkim.whiskeynote.core.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNote is a Querydsl query type for Note
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNote extends EntityPathBase<Note> {

    private static final long serialVersionUID = -167916752L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNote note = new QNote("note");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final NumberPath<Integer> briny = createNumber("briny", Integer.class);

    public final EnumPath<com.jhkim.whiskeynote.core.constant.WhiskeyColor> color = createEnum("color", com.jhkim.whiskeynote.core.constant.WhiskeyColor.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath distiller = createString("distiller");

    public final StringPath finish = createString("finish");

    public final NumberPath<Integer> floral = createNumber("floral", Integer.class);

    public final NumberPath<Integer> fruity = createNumber("fruity", Integer.class);

    public final NumberPath<Integer> herbal = createNumber("herbal", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath nose = createString("nose");

    public final QNoteBook notebook;

    public final NumberPath<Integer> peaty = createNumber("peaty", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Float> rating = createNumber("rating", Float.class);

    public final NumberPath<Integer> rich = createNumber("rich", Integer.class);

    public final NumberPath<Integer> salty = createNumber("salty", Integer.class);

    public final NumberPath<Integer> smokey = createNumber("smokey", Integer.class);

    public final NumberPath<Integer> spicy = createNumber("spicy", Integer.class);

    public final NumberPath<Integer> sweet = createNumber("sweet", Integer.class);

    public final StringPath taste = createString("taste");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> vanilla = createNumber("vanilla", Integer.class);

    public final QWhiskey whiskey;

    public final StringPath whiskey_name = createString("whiskey_name");

    public final NumberPath<Integer> woody = createNumber("woody", Integer.class);

    public QNote(String variable) {
        this(Note.class, forVariable(variable), INITS);
    }

    public QNote(Path<? extends Note> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNote(PathMetadata metadata, PathInits inits) {
        this(Note.class, metadata, inits);
    }

    public QNote(Class<? extends Note> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notebook = inits.isInitialized("notebook") ? new QNoteBook(forProperty("notebook"), inits.get("notebook")) : null;
        this.whiskey = inits.isInitialized("whiskey") ? new QWhiskey(forProperty("whiskey")) : null;
    }

}

