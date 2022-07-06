
-- 테스트용 회원 데이터
insert into triple.member(mno, user_id, reg_dt, upd_dt) values(999994, UUID_TO_BIN('ef540baf-d99e-4e99-97ab-179099e1c1e7'), now(6), now(6));
insert into triple.member(mno, user_id, reg_dt, upd_dt) values(999995, UUID_TO_BIN('0615af95-20bc-4054-9b60-8dba9928c22f'), now(6), now(6));
insert into triple.member(mno, user_id, reg_dt, upd_dt) values(999996, UUID_TO_BIN('4f2c6c16-0231-4d8e-9bc2-1f64f6e671dd'), now(6), now(6));
insert into triple.member(mno, user_id, reg_dt, upd_dt) values(999997, UUID_TO_BIN('98abfce5-6540-41a4-9c89-0ffa06c6e05f'), now(6), now(6));
insert into triple.member(mno, user_id, reg_dt, upd_dt) values(999998, UUID_TO_BIN('45b29c7a-afa6-4c81-af8d-d8f3b10121a0'), now(6), now(6));
insert into triple.member(mno, user_id, reg_dt, upd_dt) values(999999, UUID_TO_BIN('3ede0ef2-92b7-4817-a5f3-0c575361f745'), now(6), now(6));

-- 테스트용 회원 적립 데이터
insert into triple.total_point(mno, earn_total_amt, deduct_total_amt, reg_dt, upd_dt) values(999994, 0, 0, now(6), now(6));
insert into triple.total_point(mno, earn_total_amt, deduct_total_amt, reg_dt, upd_dt) values(999995, 0, 0, now(6), now(6));
insert into triple.total_point(mno, earn_total_amt, deduct_total_amt, reg_dt, upd_dt) values(999996, 0, 0, now(6), now(6));
insert into triple.total_point(mno, earn_total_amt, deduct_total_amt, reg_dt, upd_dt) values(999997, 0, 0, now(6), now(6));
insert into triple.total_point(mno, earn_total_amt, deduct_total_amt, reg_dt, upd_dt) values(999998, 0, 0, now(6), now(6));
insert into triple.total_point(mno, earn_total_amt, deduct_total_amt, reg_dt, upd_dt) values(999999, 0, 0, now(6), now(6));

-- 테스트용 장소 데이터
insert into triple.place(place_id, mno, reg_dt, upd_dt) values(UUID_TO_BIN('11ecfb56-c15a-1c46-8b49-4113d19dad89'), 999999, now(6), now(6));
insert into triple.place(place_id, mno, reg_dt, upd_dt) values(UUID_TO_BIN('11ecfb56-c168-2607-8b49-dfe4239c2faf'), 999999, now(6), now(6));
insert into triple.place(place_id, mno, reg_dt, upd_dt) values(UUID_TO_BIN('11ecfb56-c172-d468-8b49-d5df7fd01594'), 999999, now(6), now(6));
insert into triple.place(place_id, mno, reg_dt, upd_dt) values(UUID_TO_BIN('11ecfb56-c179-8b29-8b49-831f4e169e79'), 999999, now(6), now(6));
insert into triple.place(place_id, mno, reg_dt, upd_dt) values(UUID_TO_BIN('11ecfb56-c181-ef9a-8b49-712075a48633'), 999999, now(6), now(6));
insert into triple.place(place_id, mno, reg_dt, upd_dt) values(UUID_TO_BIN('2e4baf1c-5acb-4efb-a1af-eddada31b00f'), 999999, now(6), now(6));
