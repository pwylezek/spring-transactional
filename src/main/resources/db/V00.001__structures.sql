create table public.patients
(
    id                   varchar(255)  not null,
    external_id varchar(255) not null,
    name           varchar(20) not null,
    address_id            varchar(255) not null,
    constraint patients_pk primary key (id)
);

create table public.addresses
(
    id                   varchar(255)  not null,
    address varchar(255) not null,
    constraint addresses_pk primary key (id)
);

alter table public.patients
    add constraint fk_patients_addresses foreign key (address_id)
        references public.addresses (id)
        on delete restrict on update restrict;

create table public.patient_documents
(
    content                   varchar(255)  not null,
    patient_id varchar(255) not null
);

alter table public.patient_documents
    add constraint patient_documents_patients foreign key (patient_id)
        references public.patients (id)
        on delete restrict on update restrict;