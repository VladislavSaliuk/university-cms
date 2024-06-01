INSERT INTO public.faculties (faculty_name) VALUES
('Engineering'),
('Science'),
('Arts'),
('Business'),
('Medicine'),
('Law'),
('Education'),
('Computer Science'),
('Social Sciences'),
('Health Sciences');

INSERT INTO public.groups (group_name) VALUES
('AF-17'),
('EI-21'),
('FP-09'),
('LLC-12'),
('MEC-04'),
('EF-28'),
('HEG-33'),
('ADC-15'),
('ESC-19'),
('FLS-06');

INSERT INTO public.students (login, password, email, first_name, last_name, faculty_id, group_id) VALUES
('student_john_doe', 'password123', 'john_doe@student.com',  'John', 'Doe',  1, 1),
('student_jane_smith', 'pass987', 'jane_smith@student.com',  'Jane', 'Smith',  2, 2),
('student_david_johnson', 'securePWD1', 'david_johnson@student.com',  'David', 'Johnson',  1, 3),
('student_emily_brown', 'mySecret456', 'emily_brown@student.com',  'Emily', 'Brown',  3, 4),
('student_michael_wilson', 'hidden789', 'michael_wilson@student.com', 'Michael', 'Wilson',  2, 5),
('student_sarah_parker', 'safePWD3', 'sarah_parker@student.com', 'Sarah', 'Parker',  1, 6),
('student_robert_young', 'pass4All', 'robert_young@student.com', 'Robert', 'Young',  3, 7),
('student_amanda_scott', 'trust1nMe', 'amanda_scott@student.com',  'Amanda', 'Scott', 2, 8),
('student_james_harris', 'un1quePWD', 'james_harris@student.com', 'James', 'Harris',  1, 9),
('student_laura_thomas', 'key2Success', 'laura_thomas@student.com',  'Laura', 'Thomas',  3, 10);

INSERT INTO public.subjects (subject_name, subject_description, time) VALUES
('Mathematics', 'Study of numbers, quantities, shapes, and patterns.', '10:15'),
('Physics', 'Study of matter, energy, and the fundamental forces of nature.', '11:30'),
('Computer Science', 'Study of algorithms, data structures, and computation.', '13:45'),
('Biology', 'Study of living organisms and their interactions.', '14:20'),
('Chemistry', 'Study of the composition, structure, properties, and reactions of matter.', '15:10'),
('Literature', 'Study of written works, including novels, poems, and plays.', '16:25'),
('History', 'Study of past events, particularly in human affairs.', '09:00'),
('Economics', 'Study of production, distribution, and consumption of goods and services.', '11:55'),
('Psychology', 'Study of mind and behavior.', '12:40'),
('Sociology', 'Study of society, social institutions, and social relationships.', '14:55');

INSERT INTO public.teachers (login, password, email, first_name, last_name, faculty_id, group_id) VALUES
('teacher_john_doe', 'password123', 'john_doe@example.com', 'John', 'Doe',  1, 1),
('teacher_jane_smith', 'p@ssword987', 'jane_smith@example.com',  'Jane', 'Smith', 2, 2),
('teacher_david_johnson', 'securePWD1', 'david_johnson@example.com', 'David', 'Johnson', 1, 3),
('teacher_emily_brown', 'mySecret456', 'emily_brown@example.com',  'Emily', 'Brown', 3, 4),
('teacher_michael_wilson', 'hidden789', 'michael_wilson@example.com',  'Michael', 'Wilson',2, 5),
('teacher_sarah_parker', 'safePWD3', 'sarah_parker@example.com', 'Sarah', 'Parker', 1, 6),
('teacher_robert_young', 'pass4All', 'robert_young@example.com',  'Robert', 'Young', 3, 7),
('teacher_amanda_scott', 'trust1nMe', 'amanda_scott@example.com',  'Amanda', 'Scott',  2, 8),
('teacher_james_harris', 'un1quePWD', 'james_harris@example.com',  'James', 'Harris',  1, 9),
('teacher_laura_thomas', 'key2Success', 'laura_thomas@example.com',  'Laura', 'Thomas', 3, 10);

