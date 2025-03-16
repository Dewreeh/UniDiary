import React from 'react';

function Table({ data, columnMapping }) {
  return (
    <table className="table table-hover">
      <thead className="custom-thead">
        <tr>
          {data.headers.map((header) => <th key={header}>{header}</th>)}
        </tr>
      </thead>
      <tbody className="custom-row">
        {data.data && data.data.length > 0 ? (
          data.data.map((item, index) => (
            <tr key={index} className={`custom-row r${index + 1}`}>
              {data.headers.map((header) => {
                if (header === "#") return <td key={header}>{index + 1}</td>;
                const value = item[columnMapping[header]];
                return (
                  <td key={header}>
                    {typeof value === "object" && value !== null ? value.name : value}
                  </td>
                );
              })}
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan={data.headers.length}>Нет данных</td>
          </tr>
        )}
      </tbody>
    </table>
  );
}

export default Table;
