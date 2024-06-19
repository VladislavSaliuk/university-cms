INSERT INTO public.courses (course_name, course_description) VALUES
('Mathematics', 'Study of numbers, quantities, shapes, and patterns.'),
('Physics', 'Study of matter, energy, and the fundamental forces of nature.'),
('Computer Science', 'Study of algorithms, data structures, and computation.'),
('Biology', 'Study of living organisms and their interactions.'),
('Chemistry', 'Study of the composition, structure, properties, and reactions of matter.'),
('Literature', 'Study of written works, including novels, poems, and plays.'),
('History', 'Study of past events, particularly in human affairs.'),
('Economics', 'Study of production, distribution, and consumption of goods and services.'),
('Psychology', 'Study of mind and behavior.'),
('Sociology', 'Study of society, social institutions, and social relationships.');


INSERT INTO public.roles (role_name) VALUES
('ADMIN'),
('TEACHER'),
('STUDENT'),
('STUFF');


-- INSERTING TEACHERS
INSERT INTO public.users (username, password, email, first_name, last_name, role_id) VALUES
('jdoe', 'password123', 'jdoe@example.com', 'John', 'Doe', 2),
('asmith', 'password456', 'asmith@example.com', 'Alice', 'Smith', 2),
('bjohnson', 'password789', 'bjohnson@example.com', 'Bob', 'Johnson', 2),
('cmiller', 'password321', 'cmiller@example.com', 'Chris', 'Miller', 2),
('dlee', 'password654', 'dlee@example.com', 'David', 'Lee', 2),
('ewilson', 'password987', 'ewilson@example.com', 'Eva', 'Wilson', 2),
('ffranklin', 'password111', 'ffranklin@example.com', 'Frank', 'Franklin', 2),
('gmurphy', 'password222', 'gmurphy@example.com', 'Grace', 'Murphy', 2),
('hclark', 'password333', 'hclark@example.com', 'Henry', 'Clark', 2),
('ijames', 'password444', 'ijames@example.com', 'Isabella', 'James', 2);


-- INSERTING STUDENTS
INSERT INTO public.users (username, password, email, first_name, last_name, role_id) VALUES
('kjones', 'password555', 'kjones@example.com', 'Kyle', 'Jones', 3),
('lmorris', 'password666', 'lmorris@example.com', 'Linda', 'Morris', 3),
('mthompson', 'password777', 'mthompson@example.com', 'Michael', 'Thompson', 3),
('ndavis', 'password888', 'ndavis@example.com', 'Nancy', 'Davis', 3),
('owhite', 'password999', 'owhite@example.com', 'Oliver', 'White', 3),
('pphillips', 'password000', 'pphillips@example.com', 'Patricia', 'Phillips', 3),
('qyoung', 'passwordabc', 'qyoung@example.com', 'Quinn', 'Young', 3),
('rhill', 'passworddef', 'rhill@example.com', 'Rachel', 'Hill', 3),
('sscott', 'passwordghi', 'sscott@example.com', 'Steven', 'Scott', 3),
('tgreen', 'passwordjkl', 'tgreen@example.com', 'Thomas', 'Green', 3);