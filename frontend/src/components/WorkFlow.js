import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { request } from '../api/api';
import FacultyTable from './Admin/FacultyTable';
import StaffTable from './Admin/StaffTable';
import GroupsTable from './DeanStaff/GroupsTable';

function WorkFlow() {
  const { section } = useParams();
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const apiMap = {
      fakultety: '/api/get_faculties',
      'sotrudniki-dekanatov': '/api/get_staff'
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
    </div>
  );
}

export default WorkFlow;
