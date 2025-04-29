import React from "react";
import { Link } from "react-router-dom";

function Table({ data, columnMapping, headers, getRowLink }) {
  const resolvedHeaders = headers || data?.headers || Object.keys(columnMapping || {});
  const resolvedData = Array.isArray(data) ? data : data?.data || [];

  return (
    <table className="table table-hover">
      <thead className="custom-thead">
        <tr>
          {resolvedHeaders.map((header) => (
            <th key={header}>{header}</th>
          ))}
        </tr>
      </thead>
      <tbody className="custom-row">
        {resolvedData.length > 0 ? (
          resolvedData.map((item, index) => {
            const rowLink = getRowLink ? getRowLink(item) : null;
            const rowKey = item.id || index;

            return rowLink ? (
              <Link key={rowKey} to={rowLink} className="table-row-link">
                <tr className={`custom-row r${index + 1}`}>
                  {resolvedHeaders.map((header) => {
                    if (header === "#") return <td key={`${rowKey}-${header}`}>{index + 1}</td>;
                    
                    const value = columnMapping 
                      ? item[columnMapping[header]] 
                      : item[header];
                    
                    return (
                      <td key={`${rowKey}-${header}`}>
                        {renderCell(value, header)}
                      </td>
                    );
                  })}
                </tr>
              </Link>
            ) : (
              <tr key={rowKey} className={`custom-row r${index + 1}`}>
                {resolvedHeaders.map((header) => {
                  if (header === "#") return <td key={`${rowKey}-${header}`}>{index + 1}</td>;
                  
                  const value = columnMapping 
                    ? item[columnMapping[header]] 
                    : item[header];
                  
                  return (
                    <td key={`${rowKey}-${header}`}>
                      {renderCell(value, header)}
                    </td>
                  );
                })}
              </tr>
            );
          })
        ) : (
          <tr>
            <td colSpan={resolvedHeaders.length}>Нет данных</td>
          </tr>
        )}
      </tbody>
    </table>
  );
}


function renderCell(value, header) {
  if (header === "Группы" && Array.isArray(value)) {
    return value.map(g => g.name).join(', ');
  }
  
  if (typeof value === "object" && value !== null) {
    return value.name || JSON.stringify(value);
  }
  
  return value;
}

export default Table;