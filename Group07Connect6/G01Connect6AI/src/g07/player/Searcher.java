package g07.player;

import core.board.PieceColor;

import java.util.ArrayList;

import static core.board.PieceColor.*;


/**
 * Group07����������
 * ˼·��
 * ��û����4����5
 * ��û��˫��
 */
public class Searcher {
    private ArrayList<Step> solution = new ArrayList<>();
    private PieceColor myColor;
    private Step step;
    private int searchDepth = 3;
    private int[][] dir = { // dir[col][row]
            {0, 1}, // ��
            {1, 0}, // ��
            {-1, 1}, // ����
            {1, 1} // ����
    };
    public Searcher(PieceColor myColor) {
        this.myColor = myColor;
    }


    public Step getStep() {
        return step;
    }


    /**
     * ����һ��·������������6��������Ҫ������λ�á�
     *
     * @param pos     ·�����
     * @param board   ����
     * @param road    Ҫ������·
     * @param myColor ����������ɫ
     */
    private void findWin(int pos, BoardG07 board, Road road, PieceColor myColor) {
        int oriCol = pos % 19;
        int oriRow = pos / 19;
        //����·�ķ���������
        int d = road.getDir() - 1;//��������
        int dirCol = dir[d][0]; // ��
        int dirRow = dir[d][1]; // ��
        ArrayList<int[]> step = new ArrayList<>();
        int num = road.getNumOfColor(myColor);

        for (int k = 0; k < 6; ++k) {
            int col = oriCol + dirCol * k;
            int row = oriRow + dirRow * k;

            if (board.isLegalAndEmpty(col, row)) {
                int[] arr = {col, row};
                step.add(arr);
            }
        }
        // �����һ�����ϲ���������6�����ӣ���֤������·û�б�ʤ��
        if (6 != num + step.size()) step.clear();
        //���ֻ��һ������Ӯ����һ���ҿհ�λ����
        if (1 == step.size()) {
            for (int col = 0; col < 19; ++col) {
                for (int row = 0; row < 19; ++row) {
                    if (EMPTY == board.get(row * 19 + col)) {
                        int[] arr = {col, row};
                        step.add(arr);
                        row = 20;
                        col = 20;//��������ѭ��
                    }
                }
            }
        }
//        if (false) { // �滻����
//            int index[] = step.get(0);
//            int index2[] = step.get(1);
//            Move move = new Move(index[0] + index[1] * 19, index2[0] + index2[1] * 19);
//        }
        if (step.size() != 0)
            solution.add(new Step(step.get(0), step.get(1)));
    }

    /**
     * �ж�color����û�б�ʤ�壬����˫������4,��5
     *
     * @param board
     * @param color
     * @return ����б�ʤ�壬���أ����򷵻س����ֵ����û�б�ʤ��
     */
    public Step willWin(BoardG07 board, PieceColor color) {
        solution.clear();
        // �õ�������4����5���������ӵ�·
        RoadList roadLists = board.getRoadLists();
        ArrayList<Road> roads = new ArrayList<>();

        if (BLACK == color) {
            roads.addAll(roadLists.getRoad(4, 0));
            roads.addAll(roadLists.getRoad(5, 0));
        } else {
            roads.addAll(roadLists.getRoad(0, 4));
            roads.addAll(roadLists.getRoad(0, 5));
        }
        // System.out.println(roads.size());
        for (Road road : roads) {
            findWin(road.getPos(), board, road, color);
            if (0 != solution.size()) {
                System.out.println("find the answer");
                break;
            }
        }
        if (solution.size() != 0)
            return solution.get(0);
        else
            return null;
    }

    /**
     * @param roads   ·���б�
     * @param pieces  ����ar�����ӵļ���
     * @param board   ����
     * @param mychess �ҷ�������ɫ
     */
    public void findStop(ArrayList<Road> roads, ArrayList<Integer> pieces, BoardG07 board, PieceColor mychess) {
        ArrayList<Road> stopRoads = new ArrayList<>();
        for (int i = 0; i < pieces.size(); ++i) {
            for (int j = i + 1; j < pieces.size(); ++j) {
                boolean ok = true;
                stopRoads.clear();
                stopRoads.addAll(board.getRoadLists().findRoads(pieces.get(i)));
                stopRoads.addAll(board.getRoadLists().findRoads(pieces.get(j)));

                for (Road r : roads) {
                    if (!stopRoads.contains(r)) {
                        ok = false;
                        break;
                    }
                }
                if (true == ok) {
                    solution.add(new Step(pieces.get(i), pieces.get(j)));
                    return;
                }
            }
        }
//        if (false == ok) {
//            solution.add(new Step(pieces.get(tmp1), pieces.get(tmp2)));
//        }
    }

    // �ж϶�����û��ɱ��
    public Step needStop(BoardG07 board, PieceColor color, BoardScore BS) {
        //�����ӱض�
        solution.clear();

        RoadList roadLists = board.getRoadLists();
        ArrayList<Road> roads = new ArrayList<>();
        ArrayList<Road> roads2 = new ArrayList<>();
        if (BLACK == color) {
            roads.addAll(roadLists.getRoad(0, 4));
            roads.addAll(roadLists.getRoad(0, 5));
            roads2.addAll(roadLists.getRoad(0, 3));
        } else {
            roads.addAll(roadLists.getRoad(4, 0));
            roads.addAll(roadLists.getRoad(5, 0));
            roads2.addAll(roadLists.getRoad(3, 0));
        }

        ArrayList<Integer> positions = new ArrayList<>();
        ArrayList<Integer> positions2 = new ArrayList<>();
        for (Road road : roads) {
            int d = road.getDir() - 1;//��������
            int oriCol = road.getPos() % 19;
            int oriRow = road.getPos() / 19;
            for (int k = 0; k < 6; ++k) {
                int col = oriCol + dir[d][0] * k;
                int row = oriRow + dir[d][1] * k;
                if (board.isLegalAndEmpty(col, row)) {
                    if (!positions.contains(row * 19 + col))
                        positions.add(row * 19 + col);
                }
            }
        }
        this.findStop(roads, positions, board, color);
        if (0 != solution.size()) {
            return solution.get(0);
        } else {
            //�����ض�
            for (Road road : roads2) {
                int d = road.getDir() - 1;//��������
                int oriCol = road.getPos() % 19;
                int oriRow = road.getPos() / 19;
                for (int k = 0; k < 6; k++) {
                    int col = oriCol + dir[d][0] * k;
                    int row = oriRow + dir[d][1] * k;

                    if (board.isLegalAndEmpty(col, row)) {
                        if (!positions2.contains(row * 19 + col))
                            positions2.add(row * 19 + col);
                    }
                }
            }
            this.findStop(roads2, positions2, board, color);
            if (solution.size() != 0) {
                return solution.get(0);
            } else
                return null;
        }
    }

    // ������Ҫ��
    public long alphabeta(int depth, long alpha, long beta, BoardG07 board) {
        BoardScore boardScore = board.getBoardScore();
        // �ﵽ�����ȣ�����Ҷ�ӽ���ֵ������
        if (depth == searchDepth) {
            boardScore.calcScore(board.getRoadLists());
            return boardScore.getScore();
        }

        long tmp;
        long maxValue = -Long.MAX_VALUE, minValue = Long.MAX_VALUE;
        int choose = 0;
        PieceColor nowChess =
                (0 == depth % 2) ? this.myColor : this.myColor.opposite();
        GenerateCandidates generateCandts = new GenerateCandidates();
        generateCandts.generateStep(nowChess, board);
        //ȡ����������
        ArrayList<Step> steps = generateCandts.getAllStep();

        for (int i = 0; i < steps.size(); i++) {
            Step step = steps.get(i);
            board.make(step.getFirst(), step.getSecond(), nowChess);
            tmp = alphabeta(depth + 1, alpha, beta, board);
            board.unmake(step.getFirst(), step.getSecond());
            // ��ȡ�������
            if (depth == 0) {
                if (tmp > maxValue) {
                    choose = i;
                }
            }
            if (tmp > maxValue) {
                maxValue = tmp;
            }
            if (tmp < minValue) {
                minValue = tmp;
            }
            // ��֦
            if (nowChess != this.myColor) {
                if (tmp < alpha) break;
                if (tmp < beta) beta = tmp;
            }
            else {
                if (beta < tmp) break;
                if (alpha < tmp) alpha = tmp;
            }

        }
        //��ǰ�����������ѷ���
        if (depth == 0) {
            System.out.println("steps.size = " + steps.size() + " choose =  " + choose);
            this.step = steps.get(choose);
        }
        return (nowChess == this.myColor) ? maxValue : minValue;
    }
}
