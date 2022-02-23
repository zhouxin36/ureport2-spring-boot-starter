<template style="margin:5px">
</template>
<script type="text/javascript">
  window._server = "http://localhost:9095/ureport";
  import "../../../public/css/bootstrap.min.css";
  import {submitSearchForm, previewInit, loadSearchForm} from '@/preview';
  window.formElements = [];

  window.doSearch = function () {
    submitSearchForm();
  }
  window.loadSearchForm = function () {
    loadSearchForm();
  }
  export default {
    mounted: function () {
      window.vueRoute=this.vueRoute;
      previewInit()
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
