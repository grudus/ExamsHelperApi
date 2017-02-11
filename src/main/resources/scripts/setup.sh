#@IgnoreInspection BashAddShebang
db="mysql -u $1 -p$2 ExamsHelper"

cat "../sql/init.sql" | $db
echo "Created schema"

echo "Insert users"
echo "INSERT INTO users(username, password, email) VALUES
('kuba', 'kubakuba', 'kuba@kuba.com'), ('jgruda', 'tecd st123', 'jgruda@crazy.com'), ('admin', 'admin', 'admin@admin.com');" | $db

echo "Add permissions"
echo "INSERT INTO user_roles(user_id, role_id) VALUES (1, 2), (1, 1), (2, 2), (3, 1);" | $db

echo "Insert subjects"
echo "INSERT INTO subjects(label, color, user_id) VALUES
('Matematyka', '#666666', 1), ('Fizyka', '#0288D1', 1), ('Algebra', '#004D40', 1), ('Programowanie', '#388E3C', 1), ('Jezyk niemiecki', '#5D4037', 1),
('Jezyk polski', '#fa31da', 2);" | $db

echo "Insert exams"
echo "INSERT INTO exams(info, subject_id) VALUES
('Pochodne', 1), ('Calki', 1), ('Ruch prostoliniowy', 1), ('Slowka', 2), ('Dziady', 2);" | $db
