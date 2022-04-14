package com.jhkim.whiskeynote.core.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWhiskeyImage is a Querydsl query type for WhiskeyImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWhiskeyImage extends EntityPathBase<WhiskeyImage> {

    private static final long serialVersionUID = -1508836299L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWhiskeyImage whiskeyImage = new QWhiskeyImage("whiskeyImage");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public final QWhiskey whiskey;

    public QWhiskeyImage(String variable) {
        this(WhiskeyImage.class, forVariable(variable), INITS);
    }

    public QWhiskeyImage(Path<? extends WhiskeyImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWhiskeyImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWhiskeyImage(PathMetadata metadata, PathInits inits) {
        this(WhiskeyImage.class, metadata, inits);
    }

    public QWhiskeyImage(Class<? extends WhiskeyImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.whiskey = inits.isInitialized("whiskey") ? new QWhiskey(forProperty("whiskey")) : null;
    }

}

