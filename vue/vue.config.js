// vue.config.js 
module.exports = {
  baseUrl: process.env.NODE_ENV === 'production'? '/springVue': '/springVue',
  outputDir:"../target/classes/static",
  devServer: {
    port: 80,
    proxy: 'http://localhost:8080'
  },
  runtimeCompiler:true,
  productionSourceMap:false,
  lintOnSave: false
}
