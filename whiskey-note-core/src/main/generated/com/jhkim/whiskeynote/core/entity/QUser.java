package com.jhkim.whiskeynote.core.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -167704823L;

    public static final QUser user1 = new QUser("user1");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final BooleanPath accountNonExpired = createBoolean("accountNonExpired");

    public final BooleanPath accountNonLocked = createBoolean("accountNonLocked");

    public final BooleanPath admin = createBoolean("admin");

    public final CollectionPath<org.springframework.security.core.GrantedAuthority, SimplePath<org.springframework.security.core.GrantedAuthority>> authorities = this.<org.springframework.security.core.GrantedAuthority, SimplePath<org.springframework.security.core.GrantedAuthority>>createCollection("authorities", org.springframework.security.core.GrantedAuthority.class, SimplePath.class, PathInits.DIRECT2);

    public final StringPath authority = createString("authority");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath credentialsNonExpired = createBoolean("credentialsNonExpired");

    public final StringPath email = createString("email");

    public final BooleanPath enabled = createBoolean("enabled");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final BooleanPath user = createBoolean("user");

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

