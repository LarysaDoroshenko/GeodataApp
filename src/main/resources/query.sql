select distinct country
from address
where country in (select country from address group by country order by count(country) desc limit 2);

select a.country from address a group by a.country order by count(a.country) desc;

select distinct a.country 
from address a 
join place p on a.id = p.address_entity_id 
and p.longitude < 0 and a.country in (select a1.country from address a1 group by a1.country order by count(a1.country) desc);

insert into address
    (id, country)
values 
     (4, 'Україна'),
     (5, 'Україна');
commit;

insert into address
    (id, country)
values 
    (6, 'Беларусь'),
    (7, 'Беларусь');
commit;

insert into address
    (id, country)
values
    (8, 'South Africa'),
    (9, 'South Africa'),
    (10, 'South Africa');
commit;

insert into address
    (id, country)
values
    (11, 'Беларусь');
commit;

insert into address
    (id, country)
values
    (12, 'Canada'),
    (13, 'Canada'),
    (14, 'Canada');
commit;

insert into address
    (id, country)
values
    (15, 'Kalaallit Nunaat'),
    (16, 'Kalaallit Nunaat'),
    (17, 'Kalaallit Nunaat');
commit;

insert into place
    (id, longitude, address_entity_id)
values 
    (1, -56.9, 12),
    (2, -56.9, 13),
    (3, -56.9, 14),
    (4, -56.9, 15),
    (5, -56.9, 16),
    (6, -56.9, 17);
commit;