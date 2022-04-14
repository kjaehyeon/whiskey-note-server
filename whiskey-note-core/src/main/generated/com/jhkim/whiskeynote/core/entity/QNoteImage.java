package com.jhkim.whiskeynote.core.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoteImage is a Querydsl query type for NoteImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoteImage extends EntityPathBase<NoteImage> {

    private static final long serialVersionUID = -32936949L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoteImage noteImage = new QNoteImage("noteImage");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QNote note;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public QNoteImage(String variable) {
        this(NoteImage.class, forVariable(variable), INITS);
    }

    public QNoteImage(Path<? extends NoteImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoteImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoteImage(PathMetadata metadata, PathInits inits) {
        this(NoteImage.class, metadata, inits);
    }

    public QNoteImage(Class<? extends NoteImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.note = inits.isInitialized("note") ? new QNote(forProperty("note"), inits.get("note")) : null;
    }

}

