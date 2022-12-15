delete from refreshtokens;
alter table refreshtokens add constraint uk_user_id unique (user_id);
alter table refreshtokens rename to refresh_tokens;

