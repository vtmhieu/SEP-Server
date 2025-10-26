INSERT INTO EVENT (name, location, event_date, type, description, estimate_budget, status) VALUES ('Tech Summit 2025', 'Negombo', '2025-12-15', 'type1', 'Annual technology conference', '100.0', 'PLANNED');

INSERT INTO EVENT (name, location, event_date, type, description, estimate_budget, status) VALUES ('AI Expo', 'Colombo', '2025-11-20', 'type1', 'AI and Data Science Exhibition', '200.0', 'ONGOING');

INSERT INTO EVENT (name, location, event_date, type, description, estimate_budget, status) VALUES ('Developer Meetup', 'Kandy', '2025-10-30', 'type1', 'Regional developer community meetup', '130.0', 'COMPLETED');

INSERT INTO TASK (event_id, project_reference, description, assignee, subteam, comments, priority, status) VALUES ('1', 'Tech Summit 2025', 'Cater food for around 100-200 participants to have lunch during the summit', 'Mary', 'Food', 'Reasonable, no budget issues', 'HIGH', 'IN_PROGRESS');

INSERT INTO TASK (event_id, project_reference, description, assignee, subteam, comments, priority, status) VALUES ('1', 'Tech Summit 2025', 'Be able to set up around 10 tables for seating during the lunch break', 'Adam', 'Food', 'Only have 6 tables currently in stock, will need to purchase more', 'MEDIUM', 'NEEDS_MANAGER_REVIEW');

INSERT INTO TASK (event_id, project_reference, description, assignee, subteam, comments, priority, status) VALUES ('2', 'AI Expo', 'Set up the lighting/sound for all the booths at the expo', 'Sarah', 'Design', '', 'CRITICAL', 'CREATED');

INSERT INTO TASK (event_id, project_reference, description, assignee, subteam, comments, priority, status) VALUES ('3', 'Developer Meetup', 'Have some small snacks on standby for developers who attend the meet', 'Brock', 'Food', 'Small task, easily doable', 'LOW', 'IN_PROGRESS');