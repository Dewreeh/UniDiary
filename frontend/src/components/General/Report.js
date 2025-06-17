import React, { useState, useEffect } from 'react';
import { request } from '../../api/api';
import '../index.css';

function Report() {
  const [reportData, setReportData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [groups, setGroups] = useState([]);
  
  const [filters, setFilters] = useState({
    groupId: '',
    startDay: '',
    endDay: ''
  });

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const response = await request('/api/get_groups');
        setGroups(response.data || []);
      } catch (err) {
        console.error("Ошибка загрузки групп:", err);
        setError("Не удалось загрузить список групп");
      }
    };
    
    fetchGroups();
  }, []);

  const fetchReport = async () => {
    if (!filters.groupId || !filters.startDay || !filters.endDay) {
      setError("Заполните все поля фильтра");
      return;
    }

    try {
      setLoading(true);
      setError(null);
      
      const params = new URLSearchParams({
        groupId: filters.groupId,
        startDay: filters.startDay,
        endDay: filters.endDay
      });

      const response = await request(`/api/get_report?${params}`);
      setReportData(response);
    } catch (err) {
      console.error("Ошибка загрузки отчёта:", err);
      setError(err.message || "Ошибка при загрузке отчёта");
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    fetchReport();
  };

  const formatDate = (dateStr) => {
    const options = { day: 'numeric', month: 'long', year: 'numeric' };
    return new Date(dateStr).toLocaleDateString('ru-RU', options);
  };

  const renderTableRows = () => {
    return reportData.students.map(student => (
      <tr key={student.studentId}>
        <td className="sticky-col">{student.studentName}</td>
        {reportData.dates.flatMap(date => {
          const lessons = student.attendanceByDate[date] || {};
          return Object.entries(lessons).map(([lesson, isPresent]) => (
            <td 
              key={`${student.studentId}-${date}-${lesson}`}
              className={isPresent ? 'present' : 'absent'}
            >
              {isPresent ? '✓' : '✗'}
            </td>
          ));
        })}
        <td className="sticky-col-right attendance-percentage">
          {student.attendanceRate}%
        </td>
      </tr>
    ));
  };

  return (
    <div className="report-container">
      <h1 className="table-title">Отчёт по посещаемости</h1>
      
      <form onSubmit={handleSubmit} className="report-filters">
        <div className="filter-group">
          <label>Группа:</label>
          <select 
            name="groupId" 
            value={filters.groupId}
            onChange={handleFilterChange}
            required
          >
            <option value="">Выберите группу</option>
            {groups.map(group => (
              <option key={group.id} value={group.id}>{group.name}</option>
            ))}
          </select>
        </div>
        
        <div className="filter-group">
          <label>Начальная дата:</label>
          <input
            type="date"
            name="startDay"
            value={filters.startDay}
            onChange={handleFilterChange}
            required
          />
        </div>
        
        <div className="filter-group">
          <label>Конечная дата:</label>
          <input
            type="date"
            name="endDay"
            value={filters.endDay}
            onChange={handleFilterChange}
            required
            min={filters.startDay}
          />
        </div>
        
        <button type="submit" className="button" disabled={loading}>
          {loading ? 'Загрузка...' : 'Сформировать отчёт'}
        </button>
      </form>

      {error && <div className="error-message">{error}</div>}

      {reportData && (
        <div className="report-content">
          <div className="report-header">
            <h2>Группа: {groups.find(g => g.id === filters.groupId)?.name}</h2>
            <p>
              Период: с {formatDate(reportData.startDate)} по {formatDate(reportData.endDate)}
            </p>
          </div>
          
          <div className="table-wrapper">
            <div className="table-scroll">
              <table className="attendance-report-table">
                <thead>
                  <tr>
                    <th rowSpan="2" className="sticky-col">Студент</th>
                    {reportData.dates.map(date => (
                      <th key={date} colSpan={Object.keys(reportData.students[0]?.attendanceByDate[date] || {}).length}>
                        {date}
                      </th>
                    ))}
                    <th rowSpan="2" className="sticky-col-right">Посещаемость</th>
                  </tr>
                  <tr>
                    {reportData.dates.flatMap(date => 
                      Object.keys(reportData.students[0]?.attendanceByDate[date] || {}).map(lesson => (
                        <th key={`${date}-${lesson}`}>{lesson}</th>
                      ))
                    )}
                  </tr>
                </thead>
                <tbody>
                  {renderTableRows()}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Report;