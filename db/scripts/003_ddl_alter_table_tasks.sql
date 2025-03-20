ALTER TABLE Tasks
ADD user_id int references users(id);