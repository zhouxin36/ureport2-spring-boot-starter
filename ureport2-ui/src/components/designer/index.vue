<template/>
<script type="text/javascript">
window._server = "http://localhost:9095/ureport";
import designerJs from '../../index.js'
import "../../../public/css/bootstrap.min.css";
export default {
  mounted: function () {
    window.vueRoute=this.vueRoute;
    designerJs()
  },
  methods:{
    vueRoute(path,params){
      this.$router.push({path:path,query:params})
    }
  },
  watch: {
    '$route' (to, from) {
      this.$router.go(0);
    }
  }
}
</script>
