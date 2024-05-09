select p as patient, a as address from patients p, addresses a where p.address_id = a.id;

select * from patients p;
select * from addresses a;
select * from patient_documents pd;

delete from patients;
delete from addresses;
delete from patient_documents;
