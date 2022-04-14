package com.jhkim.whiskeynote.core.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWhiskey is a Querydsl query type for Whiskey
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWhiskey extends EntityPathBase<Whiskey> {

    private static final long serialVersionUID = 416499910L;

    public static final QWhiskey whiskey = new QWhiskey("whiskey");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Float> alc = createNumber("alc", Float.class);

    public final StringPath bottled_for = createString("bottled_for");

    public final StringPath bottler = createString("bottler");

    public final StringPath brand = createString("brand");

    public final EnumPath<com.jhkim.whiskeynote.core.constant.Bool3> cask_strength = createEnum("cask_strength", com.jhkim.whiskeynote.core.constant.Bool3.class);

    public final StringPath cask_type = createString("cask_type");

    public final EnumPath<com.jhkim.whiskeynote.core.constant.WhiskeyCategory> category = createEnum("category", com.jhkim.whiskeynote.core.constant.WhiskeyCategory.class);

    public final EnumPath<com.jhkim.whiskeynote.core.constant.Bool3> chillfilterd = createEnum("chillfilterd", com.jhkim.whiskeynote.core.constant.Bool3.class);

    public final EnumPath<com.jhkim.whiskeynote.core.constant.Bool3> colored = createEnum("colored", com.jhkim.whiskeynote.core.constant.Bool3.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath distillery = createString("distillery");

    public final EnumPath<com.jhkim.whiskeynote.core.constant.WhiskeyDistrict> district = createEnum("district", com.jhkim.whiskeynote.core.constant.WhiskeyDistrict.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> retail_price = createNumber("retail_price", Integer.class);

    public final EnumPath<com.jhkim.whiskeynote.core.constant.Bool3> single_cask = createEnum("single_cask", com.jhkim.whiskeynote.core.constant.Bool3.class);

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final EnumPath<com.jhkim.whiskeynote.core.constant.Bool3> small_batch = createEnum("small_batch", com.jhkim.whiskeynote.core.constant.Bool3.class);

    public final NumberPath<Integer> stated_age = createNumber("stated_age", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> vintage = createNumber("vintage", Integer.class);

    public QWhiskey(String variable) {
        super(Whiskey.class, forVariable(variable));
    }

    public QWhiskey(Path<? extends Whiskey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWhiskey(PathMetadata metadata) {
        super(Whiskey.class, metadata);
    }

}

