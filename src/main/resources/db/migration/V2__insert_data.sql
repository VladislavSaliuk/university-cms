INSERT INTO public.groups (group_name) VALUES
('AN-23'),
('EC-24'),
('AR-25'),
('HS-22'),
('BI-23'),
('PH-24'),
('LT-25'),
('EN-22'),
('MT-23'),
('SO-24');

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
('jdoe', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'jdoe@example.com', 'John', 'Doe', 2),
('asmith', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'asmith@example.com', 'Alice', 'Smith', 2),
('bjohnson', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'bjohnson@example.com', 'Bob', 'Johnson', 2),
('cmiller', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'cmiller@example.com', 'Chris', 'Miller', 2),
('dlee', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'dlee@example.com', 'David', 'Lee', 2),
('ewilson', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'ewilson@example.com', 'Eva', 'Wilson', 2),
('ffranklin', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'ffranklin@example.com', 'Frank', 'Franklin', 2),
('gmurphy', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'gmurphy@example.com', 'Grace', 'Murphy', 2),
('hclark', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'hclark@example.com', 'Henry', 'Clark', 2),
('ijames', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'ijames@example.com', 'Isabella', 'James', 2);


-- INSERTING STUDENTS
INSERT INTO public.users (username, password, email, first_name, last_name, role_id) VALUES
('kjones', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'kjones@example.com', 'Kyle', 'Jones', 3),
('lmorris', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'lmorris@example.com', 'Linda', 'Morris', 3),
('mthompson', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'mthompson@example.com', 'Michael', 'Thompson', 3),
('ndavis', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'ndavis@example.com', 'Nancy', 'Davis', 3),
('owhite', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'owhite@example.com', 'Oliver', 'White', 3),
('pphillips', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'pphillips@example.com', 'Patricia', 'Phillips', 3),
('qyoung', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'qyoung@example.com', 'Quinn', 'Young', 3),
('rhill', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'rhill@example.com', 'Rachel', 'Hill', 3),
('sscott', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'sscott@example.com', 'Steven', 'Scott', 3),
('tgreen', '$2a$12$M9Gq6/YRvOWXh1LZk/Rse./QziNsISOKDi.A986LPMuk7Haz17dCK', 'tgreen@example.com', 'Thomas', 'Green', 3);