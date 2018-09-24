import axios from 'axios'
import qs from 'qs';
import router from '@/build/router'
let fetch = axios.create({
  baseURL: process.env.BASE_URL,
  withCredentials: true,
  transformRequest: [function (data) {
    return qs.stringify(data);
  }],
  paramsSerializer: function(params) {
    return qs.stringify(params, {arrayFormat: 'brackets'})
  },
  headers:{
   /* 'Authorization':'Bearer '*/
    'Accept':'application/json',
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Credentials': true,
    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
    'X-Requested-with':'XMLHttpRequest'
  }
})

fetch.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  // 如果是401 就切到登陆页面
  if(error.response && error.response.data && error.response.data.status == 401){
    router.push('/login')
  }
  return Promise.reject(error);
});
export default  fetch