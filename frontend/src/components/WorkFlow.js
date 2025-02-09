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
  
  const sectionToRusTitle = {
    fakultety: "Факультеты",
    'sotrudniki-dekanatov': "Сотрудники деканатов",
    statistika: "Статистика",
  };

  console.log('Current section:', section); 

  useEffect(() => {
    console.log('useEffect is triggered. Section:', section); 
    
    const sectionToApiMap = {
        fakultety: '/api/faculties',
        'sotrudniki-dekanatov': '/api/staff',
        statistika: '/api/statistics',
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
  

  if (loading) {
    return <div>Загрузка...</div>;
  }



  return (
    <div className="workflow">
      {error && <div>Ошибка: {error.message}</div>}
      <InfoTable title={sectionToRusTitle[section]} data={data} />
    </div>
  );
}

export default WorkFlow;
