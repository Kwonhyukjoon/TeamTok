package com.test.shareproject.model;

public class CommentReq {

    private int cmt_no;
    private int user_id;
    private int board_id;
    private int question_id;
    private int parent;
    private int seq;
    private String comment;
    private String nickname;
    private String created_at;
    private String email;

    public CommentReq(){
    }


    public CommentReq(int cmt_no, int board_id, String comment) {
        this.cmt_no = cmt_no;
        this.board_id = board_id;
        this.comment = comment;
    }

    public CommentReq(int cmt_no, int question_id, String comment , int seq) {
        this.cmt_no = cmt_no;
        this.question_id = question_id;
        this.comment = comment;
    }

    public CommentReq(int board_id) {
        this.board_id = board_id;
    }

    public CommentReq(int question_id, int seq , int parent) {
        this.question_id = question_id;
    }

    public CommentReq(int cmt_no , String comment){
        this.cmt_no = cmt_no;
        this.comment = comment;
    }


    public CommentReq(int cmt_no , int board_id){
        this.cmt_no = cmt_no;
        this.board_id = board_id;
    }

    public CommentReq(int cmt_no , int question_id , String comment , String created_at){
        this.cmt_no = cmt_no;
        this.question_id = question_id;
        this.comment = comment;
    }

    public CommentReq(int cmt_no , int question_id , int seq , int parent){
        this.cmt_no = cmt_no;
        this.question_id = question_id;
    }





    public int getCmt_no() {
        return cmt_no;
    }

    public void setCmt_no(int cmt_no) {
        this.cmt_no = cmt_no;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
