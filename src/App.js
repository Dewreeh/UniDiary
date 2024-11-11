import Header from './Header'
import FormUser from './FormUser'
import  './index.css';

function App() {
  return (

      <div className="App">
        <div className="body">
  
          <Header />
          <div className="login-form">
            <FormUser />
          </div>
        </div>
        <p className="AdminEntryButton"> Вход для администратора </p>
      </div>

  );
}

export default App;
