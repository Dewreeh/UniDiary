import React, { useState } from 'react';
import '../index.css';
import { request } from '../../api/api';
import Table from '../Table';

function SemesterTable({ title, data, onAdd }) {
  const columnMapping = {
    "Дата начала": "startDate",
    "Дата окончания": "endDate",
    "Текущий семестр": "isCurrent"
  };

  const [newItem, setNewItem] = useState({
    startDate: '',
    endDate: '',
    isCurrent: false
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setNewItem(prevState => ({
      ...prevState,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleAdd = async () => {
    try {
      const payload = {
        startDate: newItem.startDate,
        endDate: newItem.endDate,
        isCurrent: newItem.isCurrent
      };

      await request('/api/add_semester', 'POST', payload);

      alert("Семестр успешно добавлен!");
      setNewItem({
        startDate: '',
        endDate: '',
        isCurrent: false
      });

      if (onAdd) {
        onAdd();
      }
    } catch (error) {
      console.error("Ошибка при добавлении семестра:", error);
      alert(error.message || "Ошибка при добавлении семестра");
    }
  };

  return (
    <div className="table-container">
      <h1 className="table-title">Семестры</h1>

      <Table data={data} columnMapping={columnMapping} />

      <div className="add-form">
        <input
          type="date"
          name="startDate"
          placeholder="Дата начала"
          value={newItem.startDate}
          onChange={handleChange}
          required
        />
        <input
          type="date"
          name="endDate"
          placeholder="Дата окончания"
          value={newItem.endDate}
          onChange={handleChange}
          required
        />
        <label>
          <input
            type="checkbox"
            name="isCurrent"
            checked={newItem.isCurrent}
            onChange={handleChange}
          />
          Текущий семестр
        </label>

        <button className="button add-button" onClick={handleAdd}>Добавить</button>
      </div>
    </div>
  );
}

export default SemesterTable;
