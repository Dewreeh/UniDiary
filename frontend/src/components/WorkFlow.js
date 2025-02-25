import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import './index.css';
import InfoTable from './InfoTable';
import { request } from '../api/api';

function WorkFlow() {
  const { section } = useParams();
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const sectionTitles = {
    fakultety: "Факультеты",
    "sotrudniki-dekanatov": "Сотрудники деканатов",
    statistika: "Статистика",
  };

  const title = sectionTitles[section] || section;

  useEffect(() => {
    console.log('useEffect is triggered. Section:', section);

    const sectionToApiMap = {
      fakultety: '/api/get_faculties',
      'sotrudniki-dekanatov': '/api/get_staff',
      statistika: '/api/get_statistics',
    };

    const apiUrl = sectionToApiMap[section] || '/api/faculties';

    request(apiUrl)
      .then((data) => {
        setData(data);
        setLoading(false);
      })
      .catch((error) => {
        setError(error);
        setLoading(false);
      });
  }, [section]);

  if (loading) return <div className='info-message'>Загрузка...</div>;
  if (error) return <div className='info-message'>Ошибка: {error.message}</div>;

  return (
    <div className="workflow">
      <InfoTable title={title} data={data} section={section} /> 
    </div>
  );
}

export default WorkFlow;
