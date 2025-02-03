import React from 'react';
import Header from './Header';
import './index.css'
import InfoTable from './InfoTable';
function WorkFlow(props){
    return (
        <div className='workflow'>
                    <InfoTable title={props.title}/>
        </div>
    );
}

export default WorkFlow;