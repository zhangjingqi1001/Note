<template>
    <div>
        <h2>学校名称：{{name}}</h2>
        <h2>学校名称：{{address}}</h2>
    </div>
</template>

<script>

export default {
    //组件名称
     name:'School',
    //数据
    data(){
        return{
            name : '齐工大',
            address:'济南'
        }
    }
}
</script>

<style scoped>

</style>
