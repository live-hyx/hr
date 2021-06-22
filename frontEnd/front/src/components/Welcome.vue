<template>
  <div>
    <iframe allowtransparency="true" frameborder="0" width="100%" height="98" scrolling="no" src="//tianqi.2345.com/plugin/widget/index.htm?s=2&z=3&t=1&v=0&d=5&bd=0&k=000000&f=&ltf=009944&htf=cc0000&q=1&e=1&a=0&c=56294&w=875&h=98&align=center"></iframe>
  <!-- 里面写eleCalendar组件-->
  <ele-calendar
    :render-content="renderContent"
    :data="datedef"
    :prop="prop"
  ></ele-calendar>
  </div>
</template>

<script>
  import eleCalendar from 'ele-calendar'
  import 'ele-calendar/dist/vue-calendar.css' //引入css
  export default {
    data(){
      return{
        datedef:[
          {"date":"2018-06-30","content":null,"cid":null},
          {"date":"2018-06-26","content":null,"cid":null},
        ],
        prop:'date' //对应日期字段名
      }
    },
    components: {
      eleCalendar
    },
    methods: {
      renderContent(h,parmas) {
        const loop = data =>{
          return (
            data.defvalue.value ? (<div><div>{data.defvalue.text}</div>
              <span  >备选项</span>
              </div>) : <div>{data.defvalue.text}</div>
          )
        }
        return (
          <div  style="min-height:60px;">
          {loop(parmas)}
          </div>
      );
      },
    }
  }
</script>
