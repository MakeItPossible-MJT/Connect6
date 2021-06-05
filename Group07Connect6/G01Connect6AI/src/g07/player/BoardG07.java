package g07.player;

import core.board.Board;
import core.board.PieceColor;
import core.game.Move;

import java.util.ArrayList;

import static core.board.PieceColor.EMPTY;


public class BoardG07 extends Board {
    private RoadList roadLists;
    private BoardScore boardScore;

    public BoardG07(PieceColor color) {
        super();
        roadLists = new RoadList(get_board());
        boardScore = new BoardScore(color);
        boardScore.calcScore(roadLists);
    }

    public RoadList getRoadLists() {
        return roadLists;
    }

    public BoardScore getBoardScore() {
        return boardScore;
    }

    /**
     * ��pos1���������������ӻ�������
     * @param pos Ҫ�����ĵ��λ��
     * @param color Ҫ��pos1���colorɫ
     */
    public void changePosColor(int pos, PieceColor color) {
        //System.out.println("pos = " + pos);
        PieceColor[] board = get_board();
        PieceColor v = board[pos]; // ��ǰλ�õ���ɫ
        // �ҵ���������������·
        ArrayList<Road> roads = roadLists.findRoads(pos);
        for (Road r : roads) {
            // ɾ����·
            roadLists.delRoad(r);
            // �����·
            if (PieceColor.EMPTY == color) { // �������ӣ�v�ǵ�ǰλ�õ���ɫ
                if (PieceColor.BLACK == v) r.setNumOfBlack(-1);
                else if (PieceColor.WHITE == v) r.setNumOfWhite(-1);
            } else if (PieceColor.BLACK == color) {
                r.setNumOfBlack(1);
            } else if (PieceColor.WHITE == color) {
                r.setNumOfWhite(1);
            }
            roadLists.addRoad(r);
        }
        board[pos] = color;
    }

    public void unmake(int pos1, int pos2) {
        changePosColor(pos1, PieceColor.EMPTY);
        changePosColor(pos2, PieceColor.EMPTY);
    }

    /**
     * �޸�·��
     * @param pos1 ��һ�����ӵ�����
     * @param pos2 �ڶ������ӵ�����
     * @param v ���ӵ���ɫ
     */
    public void make(int pos1, int pos2, PieceColor v) {
        changePosColor(pos1, v);
        changePosColor(pos2, v);
    }

    public void updateRoadLists(Move move, PieceColor color) {
        int index1 = move.index1();
        int index2 = move.index2();
        make(index1, index2, color);
    }

    /**
     * �ж������Ƿ����
     * @param col // ��
     * @param row // ��
     * @return
     */
    boolean isLegalAndEmpty(int col, int row) {
        return (0 <= row)
                && (19 >= row)
                && (0 <= col)
                && (19 >= col)
                && (PieceColor.EMPTY == get(row * 19 + col));
    }
}
