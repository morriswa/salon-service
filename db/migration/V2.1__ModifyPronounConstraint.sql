
alter table contact_info
drop constraint pronoun_values,
add  constraint pronoun_values check ( pronouns in ('H', 'S', 'T', 'N') );
