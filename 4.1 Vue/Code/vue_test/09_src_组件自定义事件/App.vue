<template>
  <div class="app">
    <h1>{{ msg }}</h1>
    <!-- 通过父组件给子组件传递函数类型的props实现：子给父传递数据 -->
    <School :getSchoolName="getSchoolName"></School>

    <!-- v-on 绑定事件,且v-on给testDemo身上绑定事件 -->
    <!-- 准确的来说是给组件实例对象vc身上绑定一个testDemo事件 -->
    <!-- 通过父组件给子组件绑定一个自定义事件实现：子给父传递数据 -->
    <!-- <Student v-on:testDemo="getStudentName"></Student> -->
    <!-- <Student ref="student" @click="show"></Student> -->
    <Student  @click.native="show"></Student>
  </div>
</template>

<script>
//引入组件
import Student from "./components/Student.vue";
import School from "./components/School.vue";

export default {
  name: "App",
  data() {
    return {
      msg: "你好啊！",
    };
  },
  methods: {
    getSchoolName(name) {
      console.log("App收到了学校名:", name);
    },
    getStudentName(name) {
      console.log("demo被调用了", name);
    },
    show() {
      alert("组件事件被触发");
    },
  },
  components: {
    Student,
    School,
  },
  // mounted() {
  //   // this.$refs.student 这一部分相当于获取到了组件实例对象
  //   // $on 表示当...时候 ,$on('testDemo')表示当testDemo事件触发的时候执行getStudentName回调函数
  //   this.$refs.student.$on("testDemo", this.getStudentName);
  // },
};
</script>

<style scoped>
.app {
  background-color: gray;
  padding: 5px;
}
</style>