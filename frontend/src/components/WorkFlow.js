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

  useEffect(() => {
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

  const handleAddEntity = (newEntity) => {
    setData([...data, newEntity]); // Локально обновляем таблицу

  };

  if (loading) {
    return <div>Загрузка...</div>;
  }

  if (error) {
    return <div>Ошибка: {error.message}</div>;
  }

  return (
    <div className="workflow">
      <InfoTable title={section} data={data} onAdd={handleAddEntity} />
    </div>
  );
}

export default WorkFlow;
