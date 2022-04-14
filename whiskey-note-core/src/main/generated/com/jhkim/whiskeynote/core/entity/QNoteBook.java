package com.jhkim.whiskeynote.core.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoteBook is a Querydsl query type for NoteBook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoteBook extends EntityPathBase<NoteBook> {

    private static final long serialVersionUID = -555457991L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoteBook noteBook = new QNoteBook("noteBook");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> blue = createNumber("blue", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> green = createNumber("green", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> red = createNumber("red", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser writer;

    public QNoteBook(String variable) {
        this(NoteBook.class, forVariable(variable), INITS);
    }

    public QNoteBook(Path<? extends NoteBook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoteBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoteBook(PathMetadata metadata, PathInits inits) {
        this(NoteBook.class, metadata, inits);
    }

    public QNoteBook(Class<? extends NoteBook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new QUser(forProperty("writer")) : null;
    }

}

