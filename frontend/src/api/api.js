
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';


export const request = async (endpoint, method = 'GET', body = null) => {
  

  const headers = {
    'Content-Type': 'application/json',
  };

  const options = {
    method,
    headers,
  };

  if (body) {
    options.body = JSON.stringify(body);
  }

  try {
    const response = await fetch(`${API_URL}${endpoint}`, options);
    if (!response.ok) {
      throw new Error(`Ошибка на сервере:` + response.message);
    }
    return await response.json();
  } catch (error) {
    throw new Error(`Ошибка при запросе: ${error}`);
  }
};
