import React from 'react';
import './index.css'
function InfoTable(props){
    return (
  
        <div className="table-container">
    
        <h1 className="table-title kirang-font">{props.title}</h1>
                
        <table className="table table-hover">
            <thead className="custom-thead">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Название</th>
                    <th scope="col">Контакты</th>
                </tr>
            </thead>
            <tbody className="custom-row">
                <tr className="custom-row r1">
                    <td>а</td>
                    <td>а</td>
                    <td>а</td>
                </tr>
                <tr className="custom-row">
                    <td>а</td>
                    <td>а</td>
                    <td>а</td>
                </tr>
                <tr className="custom-row">
                    <td>а</td>
                    <td>а</td>
                    <td>а</td>
                </tr>
            </tbody>
        </table>
        
        <button className="button add-button">Добавить</button>
    </div>
    );
}

export default InfoTable;