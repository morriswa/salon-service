
alter table contact_info
add fulltext name_search_idx (first_name, last_name);