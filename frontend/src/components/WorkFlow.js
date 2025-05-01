import React, { useState, useEffect, Route, Routes } from 'react';
import { useParams } from 'react-router-dom';
import { request } from '../api/api';
import FacultyTable from './Admin/FacultyTable';
import StaffTable from './Admin/StaffTable';
import GroupsTable from './DeanStaff/GroupsTable';
import StudentsTable from './DeanStaff/StudentsTable';
import DisciplinesTable from './DeanStaff/DisciplinesTable';
import HeadmanTable from './DeanStaff/HeadmanTable';
import LecturersTable from './DeanStaff/LecturersTable';
import SemesterTable from './Admin/SemesterTable';
import SchedulePage from './DeanStaff/SchedulePage';

function WorkFlow() {
  const { section } = useParams();
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const apiMap = {
      fakultety: '/api/get_faculties',
      'sotrudniki-dekanatov': '/api/get_staff',
      fakultety: '/api/get_faculties',
      gruppy: `/api/get_groups${localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF' ? '?userId=' + localStorage.getItem('userId') : ''}`,
      studenty: `/api/get_students${localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF' ? '?userId=' + localStorage.getItem('userId') : ''}`,
      discipliny: `/api/get_disciplines${localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF' ? '?userId=' + localStorage.getItem('userId') : ''}`,
      starosty: `/api/get_headmen${localStorage.getItem('userRole') === 'ROLE_DEAN_STAFF' ? '?userId=' + localStorage.getItem('userId') : ''}`,
      prepodavateli: '/api/get_lecturers',
      semestry: '/api/get_semesters'

      

    };

    const apiUrl = apiMap[section];

    if (apiUrl) {
      request(apiUrl)
        .then(data => {
          setData(data);
          setLoading(false);
        })
        .catch(error => {
          setError(error);
          setLoading(false);
        });
    } else {
      setLoading(false);
    }
  }, [section]);

  if (loading) return <div className='info-message'>Загрузка...</div>;
  if (error) return <div className='info-message'>Ошибка: {error.message}</div>;

  return (
    <div className="workflow">
      {section === "fakultety" && <FacultyTable data={data} onAdd={setData} />}
      {section === "sotrudniki-dekanatov" && <StaffTable data={data} onAdd={setData} />}
      {section === "gruppy" && <GroupsTable data={data} onAdd={setData} />}
      {section === "studenty" && <StudentsTable data={data} onAdd={setData} />}
      {section === "discipliny" && <DisciplinesTable data={data} onAdd={setData} />}
      {section === "starosty" && <HeadmanTable data={data} onAdd={setData} />}
      {section === "prepodavateli" && <LecturersTable data={data} onAdd={setData} />}
      {section === "semestry" && <SemesterTable data={data} onAdd={setData} />}
      {section === "raspisanie" && <SchedulePage data={data} onAdd={setData} />}

    </div>
  );
}

export default WorkFlow;
