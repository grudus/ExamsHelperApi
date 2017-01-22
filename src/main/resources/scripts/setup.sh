#@IgnoreInspection BashAddShebang
db="mysql -u $1 -p$2 ExamsHelper"

cat "../sql/init.sql" | $db
echo "Created schema"

echo "INSERT INTO users(username, password, email) VALUES
('kuba', 'kubakuba', 'kuba@kuba.com'), ('jgruda', 'tecd st123', 'jgruda@crazy.com'), ('admin', 'admin', 'admin@admin.com');" | $db
echo "Inserted users"

echo "INSERT INTO user_permissions(user_id, permission_id) VALUES (1, 2), (1, 1), (2, 2), (3, 1);" | $db
echo "Added permissions"

echo "INSERT INTO subjects(title, color, user_id) VALUES
('Matematyka', '#666666', 1), ('Fizyka', '#abcdef', 1), ('Jezyk polski', '#fa31da', 2);" | $db
echo "Inserted subjects"

echo "INSERT INTO exams(info, subject_id) VALUES
('Pochodne', 1), ('Calki', 1), ('Ruch prostoliniowy', 1), ('Slowka', 2), ('Dziady', 2);" | $db
echo "Inserted exams"