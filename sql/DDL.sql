use triple;

-- 시퀀스 
create table hibernate_sequence (
    next_val bigint
) engine=InnoDB;

insert into hibernate_sequence values ( 1 );


-- 회원 테이블
create table member (
    mno bigint not null,
    reg_dt datetime(6),
    upd_dt datetime(6),
    user_id BINARY(16) UNIQUE,
    primary key (mno)
) engine=InnoDB;

-- 장소 테이블
create table place (
    place_id BINARY(16) not null,
    reg_dt datetime(6),
    upd_dt datetime(6),
    mno bigint,
    primary key (place_id),

    foreign key (mno) references member(mno)
) engine=InnoDB;


-- 리뷰 테이블
create table review (
    review_id BINARY(16) not null,
    reg_dt datetime(6),
    upd_dt datetime(6),
    content varchar(255),
    remove_yn varchar(255) default 'N',
    user_id BINARY(16),
    place_id BINARY(16),
    primary key (review_id),

    foreign key (place_id) references place(place_id)
) engine=InnoDB;


-- 회원 적립금 테이블
create table total_point (
    mno bigint not null,
    reg_dt datetime(6),
    upd_dt datetime(6),
    deduct_total_amt integer,
    earn_total_amt integer,
    primary key (mno)
) engine=InnoDB;

-- 포인트 발생 내역 테이블
create table point (
    mno bigint not null,
    occur_seq bigint not null,
    reg_dt datetime(6),
    upd_dt datetime(6),
    accum_amt integer,
    bal_amt integer,
    occur_cause varchar(255),
    point_cd varchar(255),
    review_id BINARY(16),
    primary key (mno, occur_seq),

    foreign key (review_id) references review(review_id)
) engine=InnoDB;



-- 리뷰 첨부파일 테이블
create table review_attached_photo (
    photo_no bigint not null,
    reg_dt datetime(6),
    upd_dt datetime(6),
    photo_id BINARY(16),
    remove_yn varchar(255) default 'N',
    review_id BINARY(16) not null,
    primary key (photo_no, review_id),

    foreign key (review_id) references review(review_id)
) engine=InnoDB;