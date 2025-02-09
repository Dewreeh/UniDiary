import React from 'react';
import './index.css';

function InfoTable(props) {
  return (
    <div className="table-container">
      <h1 className="table-title ">{props.title}</h1>

      <table className="table table-hover">
        <thead className="custom-thead">
          <tr>
            <th scope="col">#</th>
            <th scope="col">Название</th>
            <th scope="col">Контакты</th>
          </tr>
        </thead>
        <tbody className="custom-row">
          {props.data.map((item, index) => (
            <tr key={item.id} className={`custom-row r${index + 1}`}>
              <td>{item.id}</td>
              <td>{item.name}</td>
              <td>{item.contact}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <button className="button add-button">Добавить</button>
    </div>
  );
}

export default InfoTable;
