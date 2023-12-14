<template>
  <div class="student">
    <h2>学生姓名：{{ name }}</h2>
    <h2>学生性别：{{ sex }}</h2>
    <button @click="sendStudentName">把学生名给App</button>
    <button @click="unbind">解绑atguigu事件</button>
  </div>
</template>

<script>
export default {
  //组件名称
  name: "Student",
  data() {
    return {
      name: "张三",
      sex: "男",
    };
  },
  methods: {
    sendStudentName() {
      //this可以拿到组件实例对象
      //$emit函数的含义是想触发哪个事件，我们指定的是testDemo事件
      //触发Student组件实例身上的testDemo事件
      //从第二个参数开始就可以传参
      this.$emit("testDemo", this.name);
      // this.$emit("click", this.name);
    },
    unbind() {
      // 这个语句只能解绑一个自定义事件
      this.$off("testDemo");
      //  this.$off(['testDemo','demo']) 解绑多个事件，有几个事件，就往里面写几个事件
      // this.$off() 解绑所有的自定义事件
    },
  },
};
</script>

<style scoped>
.student {
  background: orange;
  padding: 5px;
  margin-top: 30px;
}
</style>
