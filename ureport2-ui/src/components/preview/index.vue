<template style="margin:5px">
</template>
<script type="text/javascript">
  window._server = "http://localhost:9095/ureport";
  import "../../../public/css/bootstrap.min.css";
  import {submitSearchForm,previewInit} from '@/preview';
  window.formElements = [];
  let template;

  window.doSearch = function doSearch() {
    submitSearchForm();
  }
  export default {
    mounted: function () {
      previewInit()
    },
    created() {
      let template1 = this.$route.params.template
      let template2 = this.$route.params.template
      alert(template1)
      alert(template2)
    }
  }
</script>
